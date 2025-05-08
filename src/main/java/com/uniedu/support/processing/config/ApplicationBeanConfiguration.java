package sasdevs.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sasdevs.backend.dto.standard.*;
import sasdevs.backend.models.entities.*;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
//                If error with mapping Question, delete this
                .setAmbiguityIgnored(true);

        // Mappings for standard DTOs
        TypeMap<Question, QuestionDTO> questionToDTO = modelMapper.createTypeMap(Question.class, QuestionDTO.class);
        questionToDTO.addMappings(m -> m.map(src -> src.getTest().getId(), QuestionDTO::setTest));

        TypeMap<QuestionSubmission, QuestionSubmissionDTO> questionSubmissionToDTO = modelMapper.createTypeMap(QuestionSubmission.class, QuestionSubmissionDTO.class);
        questionSubmissionToDTO.addMappings(m -> m.map(src -> src.getQuestion().getId(), QuestionSubmissionDTO::setQuestion));
        questionSubmissionToDTO.addMappings(m -> m.map(src -> src.getResult().getId(), QuestionSubmissionDTO::setResult));

        TypeMap<Result, ResultDTO> resultToDTO = modelMapper.createTypeMap(Result.class, ResultDTO.class);
        resultToDTO.addMappings(m -> m.map(src -> src.getTest().getId(), ResultDTO::setTestId));
        resultToDTO.addMappings(m -> m.map(src -> src.getUser().getId(), ResultDTO::setUserId));

        TypeMap<TestAssignment, TestAssignmentDTO> testAssignmentToDTO = modelMapper.createTypeMap(TestAssignment.class, TestAssignmentDTO.class);
//        testAssignmentToDTO.addMappings(m -> m.map(src -> src.getTest().getId(), TestAssignmentDTO::setTestId));
        testAssignmentToDTO.addMappings(m -> m.map(src -> src.getGroup().getId(), TestAssignmentDTO::setGroupId));

        TypeMap<TestAttachment, TestAttachmentDTO> testAttachmentToDTO = modelMapper.createTypeMap(TestAttachment.class, TestAttachmentDTO.class);
        testAttachmentToDTO.addMappings(m -> m.map(src -> src.getTest().getId(), TestAttachmentDTO::setTest));
        testAttachmentToDTO.addMappings(m -> m.map(src -> src.getArticle().getId(), TestAttachmentDTO::setArticle));

        return modelMapper;
    }
}
