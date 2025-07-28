package com.dhanashri.Quiz_Service.Service;

import com.dhanashri.Quiz_Service.DTO.Request.ManualQuizDTO;
import com.dhanashri.Quiz_Service.DTO.Request.ManualQuizRequest;
import com.dhanashri.Quiz_Service.DTO.Request.QuizDTO;
import com.dhanashri.Quiz_Service.DTO.Response.CategoryDifficultyPair;
import com.dhanashri.Quiz_Service.DTO.Response.QuestionUsageResponse;
import com.dhanashri.Quiz_Service.DTO.Response.QuizDetailResponse;
import com.dhanashri.Quiz_Service.Dao.QuizDao;
import com.dhanashri.Quiz_Service.Exception.InvalidQuizActivationException;
import com.dhanashri.Quiz_Service.Exception.ResourceNotFoundException;
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
import java.util.stream.Stream;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    @Transactional
    public Quiz createNewVersion(Quiz oldQuiz, String title,String mode,int totalQuestions, List<Long> questionIds)
    {
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


    public Long createQuiz(QuizDTO quizDTO) {

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

        return quizDao.save(quiz).getQuiz_id();
    }

    public Long generateQuizManual(ManualQuizRequest manualQuizRequest) {
        List<Long> allQuestionIds = new ArrayList<>();
        for(ManualQuizDTO manualQuizDTO:manualQuizRequest.getConfigList())
        {
            ResponseEntity<?> idsResponse = "Random".equals(manualQuizDTO.getDiffLevel())
                    ? quizInterface.getQuestionForQuiz(manualQuizDTO.getCategoryId(), manualQuizDTO.getNumberOfQuestions())
                    : quizInterface.
                    getQuestionsForManualQuiz(manualQuizDTO.getCategoryId(), manualQuizDTO.getDiffLevel(),
                            manualQuizDTO.getNumberOfQuestions());

//            if(manualQuizDTO.getDiffLevel().equals("Random"))
//            {
//                idsResponse = quizInterface.getQuestionForQuiz(manualQuizDTO.getCategoryId()
//                        ,manualQuizDTO.getNumberOfQuestions());
//            }
//            else {
//                idsResponse = quizInterface.getQuestionsForManualQuiz(manualQuizDTO.getCategoryId(),
//                        manualQuizDTO.getDiffLevel(),manualQuizDTO.getNumberOfQuestions());
//            }
            Object responseBody = idsResponse.getBody();
//                System.out.println("Before idsresponse"+idsResponse.getBody());
            if (!idsResponse.getStatusCode().is2xxSuccessful() || !(idsResponse.getBody() instanceof List<?>)) {
                throw new RuntimeException("Failed to fetch questions for Manual quiz");
            }

            List<?> rawList = (List<?>) responseBody;
            List<Long> ids = rawList.stream().filter(Objects::nonNull)
                    .map(id->((Number)id).longValue())
                    .toList();

            if (ids != null) {
                allQuestionIds.addAll(ids);
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

        return quizDao.save(quiz).getQuiz_id();
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

    public List<QuestionWrapper> getQuizQuestions(Long id) {
        Quiz quiz = quizDao.findById(id).orElseThrow(()->new ResourceNotFoundException("Quiz Not Found"));
        List<QuizQuestion> quizQuestionList = quiz.getQuestions();
        List<Long> questionIds = quizQuestionList.stream()
                .map(QuizQuestion::getQuestion_id)
                .collect(Collectors.toList());

        return quizInterface.getQuestionById(questionIds).getBody();
    }

    public List<QuestionWrapper> getQuizQuestionsForPreview(Long id) {
        Quiz quiz = quizDao.findById(id).get();
        List<QuizQuestion> quizQuestionList = quiz.getQuestions();

        List<Long> questionIds = quizQuestionList.stream()
                .map(QuizQuestion::getQuestion_id)
                .collect(Collectors.toList());

        return quizInterface.getQuestionById(questionIds).getBody();
    }

    public Map<String,Long> getActiveInactiveCount() {
        Map<String, Long> result = new HashMap<>();
        result.put("active", quizDao.countAllActiveQuizzes());
        result.put("inactive", quizDao.countStandaloneInactiveQuizzes());

        return result;
    }

    public Page<Quiz> getPaginatedQuizzes(boolean isActive,int page,int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());

        return isActive
                ? quizDao.findLatestQuizzes(true, pageable)
                : quizDao.findStandaloneInactiveQuizzes(pageable);
    }

    public QuizDetailResponse getQuizDetailsById(Long quizId) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
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

        return quizDetailResponse;
    }

    @Transactional
    public Long updateQuiz(QuizDTO quizDTO) {
        if (quizDTO.getQuizId() == null)
            throw new IllegalArgumentException("Quiz ID is required");

        Quiz oldQuiz = quizDao.findById(quizDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        Set<Long> questionIds = distributeQuestions(quizDTO.getNumberOfQuestions(),quizDTO.getCategoryId());
        Quiz newQuiz = createNewVersion(oldQuiz,quizDTO.getQuizTitle(),"auto",quizDTO.getNumberOfQuestions(),new ArrayList<>(questionIds));

        return newQuiz.getQuiz_id();
    }

    @Transactional
    public Long updateManualQuiz(ManualQuizRequest manualQuizRequest) {

        if(manualQuizRequest.getQuizId() == null)
        {
            throw new IllegalArgumentException("Quiz ID is required");
        }
        Quiz oldQuiz = quizDao.findById(manualQuizRequest.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        List<Long> allQuestionIds = new ArrayList<>();
        for(ManualQuizDTO manualQuizDTO:manualQuizRequest.getConfigList())
        {
            ResponseEntity<?> idsResponse = "Random".equals(manualQuizDTO.getDiffLevel())
                ? quizInterface.getQuestionForQuiz(manualQuizDTO.getCategoryId(),
                    manualQuizDTO.getNumberOfQuestions())
                : quizInterface.getQuestionsForManualQuiz(manualQuizDTO.getCategoryId(),
                    manualQuizDTO.getDiffLevel(), manualQuizDTO.getNumberOfQuestions());

            if (!idsResponse.getStatusCode().is2xxSuccessful())
            {
                throw new RuntimeException("Failed to fetch questions");
            }

            List<Long> ids = (List<Long>) idsResponse.getBody();
            if (ids != null) {
                allQuestionIds.addAll(ids);
            }
        }
        Quiz newQuiz = createNewVersion(oldQuiz,manualQuizRequest.getQuizTitle(),"manual",manualQuizRequest.getTotalQuestions(),allQuestionIds);

        return newQuiz.getQuiz_id();
    }

    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizDao.existsById(id)) {
            throw new ResourceNotFoundException("Quiz not found with id: " + id);
        }
        quizDao.deleteById(id);

    }

    private List<Quiz> getVersionHistoryChain(Long quizId)
    {
        List<Quiz> history = new ArrayList<>();
        Quiz current = quizDao.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
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

        Quiz root = quizDao.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
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

    public List<Quiz> getQuizHistory(Long quizId) {
        Quiz start = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));
        //upward chain
        List<Quiz> upward = getVersionHistoryChain(quizId);
        //get root
        Quiz root = upward.get(upward.size()-1);

        //downward chain
        List<Quiz> downward = getAllDescendants(root.getQuiz_id());

        //combine
        Set<Long> seen = new HashSet<>();
        List<Quiz> fullChain = new ArrayList<>();
        Stream.concat(upward.stream(), downward.stream())
                .filter(q -> seen.add(q.getQuiz_id()))
                .sorted(Comparator.comparingLong(Quiz::getVersion).reversed())
                .forEach(fullChain::add);

        return fullChain;
    }

    @Transactional
    public void activateQuiz(Long quizId) {
        Quiz quizToActivate = quizDao.findById(quizId).orElseThrow(()->new RuntimeException("Quiz Not Found with Quiz Id:-"+quizId));

        //Validation 1- to check all questions are active or not
        List<Long> questions = quizToActivate.getQuestions()
                .stream()
                .map(QuizQuestion::getQuestion_id)
                .toList();

        ResponseEntity<?> validationResp = quizInterface.validateQuestions(questions);

        if(validationResp.getStatusCode() != HttpStatus.OK || !(validationResp.getBody() instanceof  Map))
        {
            throw new RuntimeException("Failed to validate questions");
        }

        Map<String,Object> validationData = (Map<String,Object>) validationResp.getBody();

        if(!Boolean.TRUE.equals((validationData.get("allActive"))))
        {
            List<Map<String,Object>> inactiveQuestions =
                    (List<Map<String, Object>>) validationData.get("inactiveQuestions");

            throw new InvalidQuizActivationException(
                    "Quiz activation failed. Some questions are inactive.",
                    inactiveQuestions
            );
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
    }

    @Transactional
    public void deactivateQuiz(Long quizId) {
        Quiz quiz = quizDao.findById(quizId).orElseThrow(()->new RuntimeException("Quiz not found"));
        quiz.setActive(false);
        quizDao.save(quiz);
    }

    public QuestionUsageResponse isQuestionUsedInActiveQuiz(Long questionId) {
        List<Quiz> activeQuizzes = quizDao.findActiveQuizzesUsingQuestion(questionId);

        if(activeQuizzes.isEmpty())
        {
            return new QuestionUsageResponse(false,Collections.emptyList());
        }

        List<String> quizList = activeQuizzes.stream()
                .map(Quiz::getQuiz_title)
                .toList();

        return new QuestionUsageResponse(true,quizList);
    }
}
