package com.dhanashri.Quiz_Service.Service;

import com.dhanashri.Quiz_Service.Dao.QuizDao;
import com.dhanashri.Quiz_Service.Feign.QuizInterface;
import com.dhanashri.Quiz_Service.Module.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    @Transactional
    public Quiz createNewVersion(Quiz oldQuiz, String title,String mode, List<Integer> questionIds)
    {
        try{
            //mark old quiz as inactcive
            oldQuiz.setActive(false);
            quizDao.save(oldQuiz);

            //create a new quiz
            Quiz newQuiz = new Quiz();
            newQuiz.setQuiz_title(title);
            newQuiz.setMode(mode);
            newQuiz.setVersion(oldQuiz.getVersion()+1);
            newQuiz.setPreviousVersionId(oldQuiz.getQuiz_id());
            newQuiz.setActive(true);

            List<QuizQuestion> quizQuestionList = questionIds.stream().map(id->{
                QuizQuestion quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion_id(id);
                quizQuestion.setQuiz(newQuiz);
                return quizQuestion;
            }).toList();

            newQuiz.setQuestions(quizQuestionList);
            return quizDao.save(newQuiz);
        }
        catch(Exception e)
        {
            e.printStackTrace(); // Optional
            throw new RuntimeException("Failed to create new quiz version", e);
        }
    }


    public ResponseEntity<?> createQuiz(QuizDTO quizDTO) {
        try{
            Set<Integer> questionIds = distributeQuestions(quizDTO.getNumberOfQuestions(),quizDTO.getCategoryId());
            Quiz quiz = new Quiz();
            quiz.setQuiz_title(quizDTO.getQuizTitle());
            quiz.setMode(quizDTO.getMode()!=null? quizDTO.getMode() : "auto");
            List<QuizQuestion> quizQuestionList = new ArrayList<>();
            assert questionIds != null;
            for(int i:questionIds)
            {
                QuizQuestion quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion_id(i);
                quizQuestion.setQuiz(quiz);
                quizQuestionList.add(quizQuestion);
            }
            System.out.println("check");
            quiz.setQuestions(quizQuestionList);
            quizDao.save(quiz);
            return new ResponseEntity<>(quiz.getQuiz_id(),HttpStatus.OK);
        }
        catch(Exception e)
        {
//          return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> generateQuizManual(ManualQuizRequest manualQuizRequest) {
        try{
//            System.out.println("inside service");
            List<Integer> allQuestionIds = new ArrayList<>();
            for(ManualQuizDTO manualQuizDTO:manualQuizRequest.getConfigList())
            {
                ResponseEntity<?> idsResponse = null;
                if(manualQuizDTO.getDiffLevel().equals("Random"))
                {
                    idsResponse = quizInterface.getQuestionForQuiz(manualQuizDTO.getCategoryId()
                            ,manualQuizDTO.getNumberOfQuestions());
                }
                else {
                    idsResponse = quizInterface.getQuestionsForManualQuiz(manualQuizDTO.getCategoryId(),
                            manualQuizDTO.getDiffLevel(),manualQuizDTO.getNumberOfQuestions());
                }

                if(idsResponse .getStatusCode().is2xxSuccessful())
                {
                    List<Integer> ids = (List<Integer>) idsResponse .getBody();
                    if (ids != null) {
                        allQuestionIds.addAll(ids);
                    }
                }
                else {
                    return new ResponseEntity<>("Failed to fetch questions for Manual quiz", HttpStatus.BAD_REQUEST);
                }
            }
            Quiz quiz = new Quiz();
            quiz.setQuiz_title(manualQuizRequest.getQuizTitle());
            quiz.setMode(manualQuizRequest.getMode()!=null? manualQuizRequest.getMode() : "manual");
            List<QuizQuestion> quizQuestionList = new ArrayList<>();

            for(int id:allQuestionIds)
            {
                QuizQuestion quizQuestion = new QuizQuestion();
//                quizQuestion.setQuiz_question_id(id);
                quizQuestion.setQuestion_id(id);
                quizQuestion.setQuiz(quiz);
                quizQuestionList.add(quizQuestion);
            }
            quiz.setQuestions(quizQuestionList);
            quizDao.save(quiz);

            return new ResponseEntity<>(quiz.getQuiz_id(),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to Generate a Manual quiz", HttpStatus.BAD_REQUEST);
        }
    }

    public Set<Integer> distributeQuestions(int totalQuestions,List<Integer> categoryId)
    {
        Set<Integer> finalQuestionsId = new HashSet<>();
        Map<Integer,Set<Integer>> categoryQuestions = new HashMap<>();

        int perCategory = totalQuestions/categoryId.size();
        int remainder = totalQuestions % categoryId.size();

        //Try to fetch per category question count
        Map<Integer,Integer> desiredCount = new HashMap<>();
        for(Integer category:categoryId)
        {
            int desired = perCategory + (remainder-- > 0?1:0);
            desiredCount.put(category,desired);
        }

        Map<Integer,Integer> shortFallMap = new HashMap<>();
        for(Integer category:categoryId)
        {
            int desired = desiredCount.get(category);
            List<Integer> fetched = quizInterface.getQuestionForQuiz(category,desired).getBody();

            if(fetched==null || fetched.isEmpty())
            {
                continue;
            }

            finalQuestionsId.addAll(fetched);
            categoryQuestions.put(category,new HashSet<>(fetched));

            int shortFall = desired-fetched.size();
            if(shortFall>0)
            {
                shortFallMap.put(category,shortFall);
            }
        }

        //redistribute remaining questions
        int stillNeeded = totalQuestions - finalQuestionsId.size();

        for(Integer category:categoryId)
        {
            if(stillNeeded<=0)break;

            Set<Integer> alreadyFetched = categoryQuestions.getOrDefault(category,new HashSet<>());
            int alreadyCount = alreadyFetched.size();
            int maxToAsk = totalQuestions;

            List<Integer> more = quizInterface.getQuestionForQuiz(category,maxToAsk).getBody();
            if(more==null || more.isEmpty())
            {
                continue;
            }

            more.removeAll(alreadyFetched);
            int takecount = Math.min(stillNeeded,more.size());
            finalQuestionsId.addAll(more.subList(0,takecount));
            stillNeeded-=takecount;
        }

        return finalQuestionsId;
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long id) {
        try{

            Quiz quiz = quizDao.findById(id).get();
            List<QuizQuestion> quizQuestionList = quiz.getQuestions();
            List<Integer> questionIds = quizQuestionList.stream()
                    .map(QuizQuestion::getQuestion_id)
                    .collect(Collectors.toList());

            List<QuestionWrapper> questionWrapperList = quizInterface.getQuestionById(questionIds).getBody();

            return ResponseEntity.ok(questionWrapperList);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }

    public ResponseEntity<?> getQuizQuestionsForPreview(Long id) {
        try{

            Quiz quiz = quizDao.findById(id).get();
            List<QuizQuestion> quizQuestionList = quiz.getQuestions();

            List<Integer> questionIds = quizQuestionList.stream()
                    .map(QuizQuestion::getQuestion_id)
                    .collect(Collectors.toList());

            List<QuestionWrapper> questionWrapperList = quizInterface.getQuestionById(questionIds).getBody();

            return new ResponseEntity<>(questionWrapperList,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Failed to return the Quesiton for preview",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getActiveInactiveCount() {
        try{
            List<QuizStatusCount> quizStatusCountList = quizDao.getQuizStatusCounts();
            Map<String, Long> result = new HashMap<>();
            result.put("active", 0L);
            result.put("inactive", 0L);
            for (QuizStatusCount qc : quizStatusCountList) {
                result.put(qc.getIsActive() ? "active" : "inactive", qc.getCount());
            }

            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to return the active inactive quiz count",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getPaginatedQuizzes(boolean isActive,int page,int size) {
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
            Page<Quiz> quizPage = quizDao.findLatestQuizzes(isActive, pageable);
            return new ResponseEntity<>(quizPage,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getQuizDetailsById(Long quizId) {
        try{
            Optional<Quiz> optionalQuiz = quizDao.findById(quizId);
            if(optionalQuiz.isEmpty())
            {
                return new ResponseEntity<>("Quiz Not Found",HttpStatus.NOT_FOUND);
            }
            System.out.println("Inside get quiz details by id");
            Quiz quiz = optionalQuiz.get();
            List<Integer> questionIds = quiz.getQuestions().stream()
                                        .map(QuizQuestion::getQuestion_id)
                                        .toList();

            ResponseEntity<List<QuestionWrapper>> response = quizInterface.getQuestionById(questionIds);
            List<QuestionWrapper> questions = response.getBody();

            Map<String, Map<String, Long>> categoryMap = questions.stream()
                    .collect(Collectors.groupingBy(QuestionWrapper::getCategory,
                            Collectors.groupingBy(QuestionWrapper::getDiff_level,Collectors.counting())));

            List<CategoryDifficultyPair> summary = new ArrayList<>();
            categoryMap.forEach((cat,diffMap)->{
                if("auto".equalsIgnoreCase(quiz.getMode())){
                    int total = diffMap.values().stream().mapToInt(Long::intValue).sum();
                    summary.add(new CategoryDifficultyPair(cat,"Random",total));
                }
                else {
                    diffMap.forEach((diff,count)->{
                        summary.add(new CategoryDifficultyPair(cat,diff,count.intValue()));
                    });
                }
            });

            QuizDetailResponse quizDetailResponse = new QuizDetailResponse();
            quizDetailResponse.setQuizId(quiz.getQuiz_id());
            quizDetailResponse.setQuizTitle(quiz.getQuiz_title());
            quizDetailResponse.setActive(quiz.isActive());
            quizDetailResponse.setCreatedAt(quiz.getCreatedAt());
            quizDetailResponse.setCategoryDifficultyPairList(summary);
            quizDetailResponse.setQuestionWrapperList(questions);
            quizDetailResponse.setMode(quiz.getMode());

            return new ResponseEntity<>(quizDetailResponse,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to fetch Quiz Details",HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<?> updateQuiz(QuizDTO quizDTO) {
        try{
            if(quizDTO.getQuizId() == null)
            {
                return ResponseEntity.badRequest().body("Quiz id is required for update");
            }
            Optional<Quiz> optionalQuiz = quizDao.findById(quizDTO.getQuizId());
            if(optionalQuiz.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz Not Found");
            }
            Quiz oldQuiz = optionalQuiz.get();

            Set<Integer> questionIds = distributeQuestions(quizDTO.getNumberOfQuestions(),quizDTO.getCategoryId());
            Quiz newQuiz = createNewVersion(oldQuiz,quizDTO.getQuizTitle(),"auto",new ArrayList<>(questionIds));

//            List<QuizQuestion> quizQuestionList = questionIds.stream().map(id->{
//                QuizQuestion quizQuestion = new QuizQuestion();
//                quizQuestion.setQuestion_id(id);
//                quizQuestion.setQuiz(quiz);
//                return quizQuestion;
//            }).toList();
//
//            quiz.setQuestions(quizQuestionList);
//            quizDao.save(quiz);
            return new ResponseEntity<>(newQuiz.getQuiz_id(),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update quiz",HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<?> updateManualQuiz(ManualQuizRequest manualQuizRequest) {
        try{
            if(manualQuizRequest.getQuizId() == null)
            {
                return ResponseEntity.badRequest().body("Quiz id is required for update");
            }
            Optional<Quiz> optionalQuiz = quizDao.findById(manualQuizRequest.getQuizId());
            if(optionalQuiz.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz Not Found");
            }


            List<Integer> allQuestionIds = new ArrayList<>();
            for(ManualQuizDTO manualQuizDTO:manualQuizRequest.getConfigList())
            {
                ResponseEntity<?> idsResponse = null;
                if(manualQuizDTO.getDiffLevel().equals("Random"))
                {
                    idsResponse = quizInterface.getQuestionForQuiz(manualQuizDTO.getCategoryId()
                            ,manualQuizDTO.getNumberOfQuestions());
                }
                else {
                    idsResponse = quizInterface.getQuestionsForManualQuiz(manualQuizDTO.getCategoryId(),
                            manualQuizDTO.getDiffLevel(),manualQuizDTO.getNumberOfQuestions());
                }

                if(idsResponse .getStatusCode().is2xxSuccessful())
                {
                    List<Integer> ids = (List<Integer>) idsResponse .getBody();
                    if (ids != null) {
                        allQuestionIds.addAll(ids);
                    }
                }
                else {
                    return new ResponseEntity<>("Failed to fetch questions for Manual quiz", HttpStatus.BAD_REQUEST);
                }
            }
            Quiz oldQuiz = optionalQuiz.get();
            Quiz newQuiz = createNewVersion(oldQuiz,manualQuizRequest.getQuizTitle(),"manual",allQuestionIds);
//            List<QuizQuestion> quizQuestionList = allQuestionIds.stream().map(id->{
//                QuizQuestion quizQuestion = new QuizQuestion();
//                quizQuestion.setQuestion_id(id);
//                quizQuestion.setQuiz(quiz);
//                return quizQuestion;
//            }).toList();
//            quiz.setQuestions(quizQuestionList);
//            quizDao.save(quiz);

            return new ResponseEntity<>(newQuiz.getQuiz_id(),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return  new ResponseEntity<>("Failed to update Manual quiz",HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteQuiz(Long id) {
        try{
            if(!quizDao.existsById(id))
            {
                return  new ResponseEntity<>("Quiz Not found by this Id",HttpStatus.NOT_FOUND);
            }
            quizDao.deleteById(id);
            return new ResponseEntity<>("Quiz Deleted successfully",HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Unable to delete the quiz.",HttpStatus.BAD_REQUEST);
        }
    }

    public List<Quiz> getQuizVersionHistory(Long quizId) {
        List<Quiz> history = new ArrayList<>();
        Quiz current = quizDao.findById(quizId).orElseThrow();
        while (current.getPreviousVersionId() != null) {
            current = quizDao.findById(current.getPreviousVersionId()).orElseThrow();
            history.add(current);
        }
        return history;
    }

}
