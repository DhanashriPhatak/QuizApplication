package com.dhanashri.Quiz_Service.Service;

import com.dhanashri.Quiz_Service.DTO.Request.ManualQuizDTO;
import com.dhanashri.Quiz_Service.DTO.Request.ManualQuizRequest;
import com.dhanashri.Quiz_Service.DTO.Request.QuizDTO;
import com.dhanashri.Quiz_Service.DTO.Response.CategoryDifficultyPair;
import com.dhanashri.Quiz_Service.DTO.Response.QuestionUsageResponse;
import com.dhanashri.Quiz_Service.DTO.Response.QuizDetailResponse;
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
    public Quiz createNewVersion(Quiz oldQuiz, String title,String mode,int totalQuestions, List<Long> questionIds)
    {
        try{
            //mark old quiz as inactcive
            oldQuiz.setActive(false);
            quizDao.save(oldQuiz);

            //create a new quiz
            Quiz newQuiz = new Quiz();
            newQuiz.setQuiz_title(title);
            newQuiz.setMode(mode);
            newQuiz.setTotal_Questions(totalQuestions);
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
            Set<Long> questionIds = distributeQuestions(quizDTO.getNumberOfQuestions(),quizDTO.getCategoryId());
            Quiz quiz = new Quiz();
            quiz.setQuiz_title(quizDTO.getQuizTitle());
            quiz.setMode(quizDTO.getMode()!=null? quizDTO.getMode() : "auto");
            quiz.setTotal_Questions(quizDTO.getNumberOfQuestions());
            List<QuizQuestion> quizQuestionList = new ArrayList<>();
            assert questionIds != null;
            for(Long i:questionIds)
            {
                QuizQuestion quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion_id(i);
                quizQuestion.setQuiz(quiz);
                quizQuestionList.add(quizQuestion);
            }
//            System.out.println("check");
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
            List<Long> allQuestionIds = new ArrayList<>();
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
                    List<Long> ids = (List<Long>) idsResponse .getBody();
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
            quiz.setTotal_Questions(manualQuizRequest.getTotalQuestions());
            quiz.setMode(manualQuizRequest.getMode()!=null? manualQuizRequest.getMode() : "manual");
            List<QuizQuestion> quizQuestionList = new ArrayList<>();

            for(Long id:allQuestionIds)
            {
                QuizQuestion quizQuestion = new QuizQuestion();
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

    public Set<Long> distributeQuestions(int totalQuestions,List<Integer> categoryId)
    {
        Set<Long> finalQuestionsId = new HashSet<>();
        Map<Integer,Set<Long>> categoryQuestions = new HashMap<>();

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
            ResponseEntity<?> response = quizInterface.getQuestionForQuiz(category,desired);
            List<?> rawList = (List<?>) response.getBody();
//            List<Long> fetched = (List<Long>) response.getBody();
            List<Long> fetched = rawList.stream()
                    .map(obj -> Long.valueOf(obj.toString()))
                    .collect(Collectors.toList());
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

            Set<Long> alreadyFetched = categoryQuestions.getOrDefault(category,new HashSet<>());
            int alreadyCount = alreadyFetched.size();
            int maxToAsk = totalQuestions;

            List<?> rawMore = (List<?>) quizInterface.getQuestionForQuiz(category,maxToAsk).getBody();
            List<Long> more = rawMore.stream()
                    .map(obj -> Long.valueOf(obj.toString()))
                    .collect(Collectors.toList());
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
            List<Long> questionIds = quizQuestionList.stream()
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

            List<Long> questionIds = quizQuestionList.stream()
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
            Long active = quizDao.countAllActiveQuizzes();
            Long inactive = quizDao.countStandaloneInactiveQuizzes();
            Map<String, Long> result = new HashMap<>();
            result.put("active", active);
            result.put("inactive", inactive);

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
            Page<Quiz> quizPage;
            if(isActive)
            {
                quizPage = quizDao.findLatestQuizzes(true, pageable);
            }
            else {
                quizPage = quizDao.findStandaloneInactiveQuizzes(pageable);
            }
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
            List<Long> questionIds = quiz.getQuestions().stream()
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

            Set<Long> questionIds = distributeQuestions(quizDTO.getNumberOfQuestions(),quizDTO.getCategoryId());
            Quiz newQuiz = createNewVersion(oldQuiz,quizDTO.getQuizTitle(),"auto",quizDTO.getNumberOfQuestions(),new ArrayList<>(questionIds));

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

            List<Long> allQuestionIds = new ArrayList<>();
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
                    List<Long> ids = (List<Long>) idsResponse .getBody();
                    if (ids != null) {
                        allQuestionIds.addAll(ids);
                    }
                }
                else {
                    return new ResponseEntity<>("Failed to fetch questions for Manual quiz", HttpStatus.BAD_REQUEST);
                }
            }
            Quiz oldQuiz = optionalQuiz.get();
            Quiz newQuiz = createNewVersion(oldQuiz,manualQuizRequest.getQuizTitle(),"manual",manualQuizRequest.getTotalQuestions(),allQuestionIds);

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

    private List<Quiz> getVersionHistoryChain(Long quizId)
    {
        List<Quiz> history = new ArrayList<>();
        Quiz current = quizDao.findById(quizId).orElseThrow();
        history.add(current);

        while(current.getPreviousVersionId()!=null)
        {
            current = quizDao.findById(current.getPreviousVersionId()).orElseThrow();
            history.add(current);
        }

        return history;
    }

    private List<Quiz> getAllDescendants(Long quizId)
    {
        List<Quiz> descendants = new ArrayList<>();
        Queue<Quiz> quizQueue = new LinkedList<>();

        Quiz root = quizDao.findById(quizId).orElseThrow();
        quizQueue.add(root);

        while(!quizQueue.isEmpty())
        {
            Quiz current = quizQueue.poll();
            List<Quiz> children = quizDao.findByPreviousVersionId(current.getQuiz_id());
            quizQueue.addAll(children);
            descendants.addAll(children);
        }
        return descendants;
    }

    public ResponseEntity<?> getQuizHistory(Long quizId) {
        try{
            //upward chain
            List<Quiz> upward = getVersionHistoryChain(quizId);
            //get root
            Quiz root = upward.get(upward.size()-1);

            //downward chain
            List<Quiz> downward = getAllDescendants(root.getQuiz_id());

            //combine
            Set<Long> seen = new HashSet<>();
            List<Quiz> fullChain = new ArrayList<>();

            for(Quiz q:upward)
            {
                if(seen.add(q.getQuiz_id()))
                {
                    fullChain.add(q);
                }
            }
            for(Quiz q:downward)
            {
                if(seen.add(q.getQuiz_id()))
                {
                    fullChain.add(q);
                }
            }
            fullChain.sort(Comparator.comparingLong(Quiz::getVersion).reversed());

            return new ResponseEntity<>(fullChain,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to fetch the Quiz History",HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<?> activateQuiz(Long quizId) {
        try{
            Quiz quizToActivate = quizDao.findById(quizId).orElseThrow(()->new RuntimeException("Quiz Not Found with Quiz Id:-"+quizId));

            //Validation 1- to check all questions are active or not
            List<Long> questions = quizToActivate.getQuestions()
                    .stream()
                    .map(QuizQuestion::getQuestion_id)
                    .toList();

            ResponseEntity<?> validationResp = quizInterface.validateQuestions(questions);

            if(validationResp.getStatusCode() != HttpStatus.OK || !(validationResp.getBody() instanceof  Map))
            {
                return new ResponseEntity<>("Failed to validate question",HttpStatus.BAD_REQUEST);
            }

            Map<String,Object> validationData = (Map<String,Object>) validationResp.getBody();
            Boolean allActive = (Boolean) validationData.get("allActive");

            if(!Boolean.TRUE.equals((allActive)))
            {
                List<Map<String,Object>> inactiveQuestions = (List<Map<String, Object>>) validationData.get("inactiveQuestions");

                Map<String,Object> response = new HashMap<>();
                response.put("error", "Cannot activate Quiz. Some Questions are inactive.");
                response.put("inactiveQuestions",inactiveQuestions);

                return  new ResponseEntity<>(response,HttpStatus.CONFLICT);
            }

            //fetch all versions in the chain including current version
            List<Quiz> upward = getVersionHistoryChain(quizId);

            //get root
            Quiz root = upward.get(upward.size()-1);

            //from root traverse down to get full version chain
            Queue<Quiz> quizQueue = new LinkedList<>();
            List<Quiz> fullChain = new ArrayList<>();
            quizQueue.add(root);

            while(!quizQueue.isEmpty())
            {
                Quiz current = quizQueue.poll();
                fullChain.add(current);

                List<Quiz> children = quizDao.findByPreviousVersionId(current.getQuiz_id());
                quizQueue.addAll(children);
            }

            //deactivate all
            fullChain.forEach(q->q.setActive(false));

            //Activate the requested quiz id
            quizToActivate.setActive(true);
            quizDao.saveAll(fullChain);
            return new ResponseEntity<>("Quiz version activated successfully", HttpStatus.OK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to activate the Quiz Version",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deactivateQuiz(Long quizId) {
        try{
            Quiz quiz = quizDao.findById(quizId).orElseThrow(()->new RuntimeException("Quiz not found"));
            quiz.setActive(false);
            quizDao.save(quiz);
            return new ResponseEntity<>("Quiz Deactivated Successfully",HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to deactivate the quiz. Please Try again.",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<QuestionUsageResponse> isQuestionUsedInActiveQuiz(Long questionId) {
        try{
            List<Quiz> activeQuizzes = quizDao.findActiveQuizzesUsingQuestion(questionId);

            if(activeQuizzes.isEmpty())
            {
                return new ResponseEntity<>(new QuestionUsageResponse(false,Collections.emptyList()),HttpStatus.OK);
            }

            List<String> quizList = activeQuizzes.stream()
                    .map(Quiz::getQuiz_title)
                    .toList();
            QuestionUsageResponse questionUsageResponse = new QuestionUsageResponse(true,quizList);
            return new ResponseEntity<>(questionUsageResponse,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new QuestionUsageResponse(false,Collections.emptyList()),HttpStatus.BAD_REQUEST);
        }
    }
}
