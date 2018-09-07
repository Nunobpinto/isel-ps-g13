package isel.leic.ps.eduWikiAPI.configuration.security.authorization

import isel.leic.ps.eduWikiAPI.configuration.security.authorization.ReputationRole.*
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.*
import org.springframework.stereotype.Component
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.FilterInvocation

/**
 * Configures authorization aspects of the Service
 */
@Component
class AuthorizationConfig {

    companion object {
        // Defines which roles include include other roles
        val ROLE_HIERARCHY = "$ROLE_DEV > $ROLE_ADMIN > $ROLE_BEGINNER"
        // Defines access control for each resource in API
        val REPUTATION_MATCHERS = mapOf(
                // AuthUserController
                (GET    to "/user/**") to ROLE_BEGINNER,
                (POST   to "/user/**") to ROLE_BEGINNER,
                (PATCH  to "/user/**") to ROLE_BEGINNER,
                (DELETE to "/user/**") to ROLE_BEGINNER,
                // Class
                (GET    to "/classes") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}") to ROLE_BEGINNER,
                (POST   to "/classes") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/vote") to ROLE_BEGINNER,
                (PATCH  to "/classes/{classId}") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/reports") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/reports") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/classes/stage") to ROLE_BEGINNER,
                (GET    to "/classes/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/classes/stage") to ROLE_BEGINNER,
                (POST   to "/classes/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/classes/stage/{stageId}/vote") to ROLE_BEGINNER,
                (DELETE to "/classes/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/versions") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/versions/{versionId}") to ROLE_BEGINNER,
                 // Class Course
                (GET    to "/classes/{classId}/courses/{courseId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/vote") to ROLE_BEGINNER,
                (DELETE to "/classes/{classId}/courses/{courseId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/courses/{courseId}/reports") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/reports") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}/courses/{courseId}/reports/{reportId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/stage") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/stage") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/stage/{stageId}/vote") to ROLE_BEGINNER,
                (DELETE to "/classes/{classId}/courses/stage/{stageId}") to ROLE_ADMIN,
                 // Lecture
                (GET    to "/classes/{classId}/courses/{courseId}/lectures") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/vote") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}") to ROLE_BEGINNER,
                (DELETE to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/stage") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/stage") to ROLE_BEGINNER,
                (DELETE to "/classes/{classId}/courses/{courseId}/lectures/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/stage/{stageId}/vote") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/lectures/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/versions") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/versions") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/lectures/{lectureId}/versions/{versionId}") to ROLE_BEGINNER,
                  // Homework
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/vote") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}") to ROLE_BEGINNER,
                (DELETE to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/stage") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/stage") to ROLE_BEGINNER,
                (DELETE to "/classes/{classId}/courses/{courseId}/homeworks/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/stage/{stageId}/vote") to ROLE_BEGINNER,
                (POST   to "/classes/{classId}/courses/{courseId}/homeworks/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions") to ROLE_BEGINNER,
                (GET    to "/classes/{classId}/courses/{courseId}/homeworks/{homeworkId}/versions/{versionId}") to ROLE_BEGINNER,
                // Course
                (GET    to "/courses") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}") to ROLE_BEGINNER,
                (POST   to "/courses") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/vote") to ROLE_BEGINNER,
                (PATCH  to "/courses/{courseId}") to ROLE_ADMIN,
                (DELETE to "/courses/{courseId}") to ROLE_ADMIN,
                (GET    to "/courses/{courseId}/reports") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/reports") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/courses/{courseId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/courses/stage") to ROLE_BEGINNER,
                (GET    to "/courses/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/courses/stage") to ROLE_BEGINNER,
                (POST   to "/courses/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/courses/stage/{stageId}/vote") to ROLE_BEGINNER,
                (DELETE to "/courses/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/courses/{courseId}/versions") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/versions/{versionId}") to ROLE_BEGINNER,
                // Course Term
                (GET    to "/courses/{courseId}/terms") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}") to ROLE_BEGINNER,
                // Course Class
                (GET    to "/courses/{courseId}/terms/{termId}/classes") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/classes/{classId}") to ROLE_BEGINNER,
                // Exams
                (GET    to "/courses/{courseId}/terms/{termId}/exams") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/{examId}") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/exams") to ROLE_ADMIN,
                (DELETE to "/courses/{courseId}/terms/{termId}/exams/{examId}") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/{examId}/vote") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}") to ROLE_BEGINNER,
                (DELETE to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/{examId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/stage") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/stage") to ROLE_BEGINNER,
                (DELETE to "/courses/{courseId}/terms/{termId}/exams/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/stage/{stageId}/vote") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/exams/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/{examId}/versions") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/{examId}/versions") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/exams/{examId}/versions/{versionId}") to ROLE_BEGINNER,
                // Work Assignments
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments") to ROLE_ADMIN,
                (DELETE to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}") to ROLE_BEGINNER,
                (DELETE to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/stage") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/stage") to ROLE_BEGINNER,
                (DELETE to "/courses/{courseId}/terms/{termId}/work-assignments/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/stage/{stageId}/vote") to ROLE_BEGINNER,
                (POST   to "/courses/{courseId}/terms/{termId}/work-assignments/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions") to ROLE_BEGINNER,
                (GET    to "/courses/{courseId}/terms/{termId}/work-assignments/{workAssignmentId}/versions/{versionId}") to ROLE_BEGINNER,
                // Organization
                (GET    to "/organization") to ROLE_BEGINNER,
                (PATCH  to "/organization") to ROLE_ADMIN,
                (GET    to "/organization/reports") to ROLE_BEGINNER,
                (GET    to "/organization/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/organization/reports") to ROLE_BEGINNER,
                (POST   to "/organization/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/organization/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/organization/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/organization/versions") to ROLE_BEGINNER,
                (GET    to "/organization/versions/{versionId}") to ROLE_BEGINNER,
                // Programme
                (GET    to "/programmes") to ROLE_BEGINNER,
                (GET    to "/programmes/{programmeId}") to ROLE_BEGINNER,
                (POST   to "/programmes") to ROLE_ADMIN,
                (POST   to "/programmes/{programmeId}/vote") to ROLE_BEGINNER,
                (PATCH  to "/programmes/{programmeId}") to ROLE_ADMIN,
                (DELETE to "/programmes/{programmeId}") to ROLE_ADMIN,
                (GET    to "/programmes/{programmeId}/reports") to ROLE_BEGINNER,
                (GET    to "/programmes/{programmeId}/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/reports") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/programmes/{programmeId}/reports/{reportId}") to ROLE_ADMIN,
                (GET    to "/programmes/stage") to ROLE_BEGINNER,
                (GET    to "/programmes/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/programmes/stage") to ROLE_BEGINNER,
                (POST   to "/programmes/stage/{stageId}") to ROLE_ADMIN,
                (POST   to "/programmes/stage/{stageId}/vote") to ROLE_BEGINNER,
                (DELETE to "/programmes/stage/{stageId}") to ROLE_ADMIN,
                (GET    to "/programmes/{programmeId}/versions") to ROLE_BEGINNER,
                (GET    to "/programmes/{programmeId}/versions/{versionId}") to ROLE_BEGINNER,
                // Programme Course
                (GET    to "/programmes/{programmeId}/courses/{courseId}") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/courses/{courseId}") to ROLE_ADMIN,
                (POST   to "/programmes/{programmeId}/courses/{courseId}/vote") to ROLE_BEGINNER,
                (DELETE to "/programmes/{programmeId}/courses/{courseId}") to ROLE_ADMIN,
                (GET    to "/programmes/{programmeId}/courses/{courseId}/reports") to ROLE_BEGINNER,
                (GET    to "/programmes/{programmeId}/courses/{courseId}/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/courses/{courseId}/reports") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/courses/{courseId}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/programmes/{programmeId}/courses/{courseId}/reports/{reportId}") to ROLE_ADMIN,
                (POST   to "/programmes/{programmeId}/courses/{courseId}/reports/{reportId}/vote") to ROLE_BEGINNER,
                (GET    to "/programmes/{programmeId}/courses/stage") to ROLE_BEGINNER,
                (GET    to "/programmes/{programmeId}/courses/stage/{stageId}") to ROLE_BEGINNER,
                (POST   to "/programmes/{programmeId}/courses/{courseId}/stage") to ROLE_ADMIN,
                (POST   to "/programmes/{programmeId}/courses/stage/{stageId}/vote") to ROLE_BEGINNER,
                (DELETE to "/programmes/{programmeId}/courses/stage/{stageId}") to ROLE_ADMIN,
                // Tenants
                (POST   to "/tenants/pending") to ROLE_DEV,
                (POST   to "/tenants/pending/{tenantUuid}") to ROLE_DEV,
                (DELETE to "/tenants/pending/{tenantUuid}") to ROLE_DEV,
                // Terms
                (GET    to "/terms/") to ROLE_BEGINNER,
                (GET    to "/terms/{termId}") to ROLE_BEGINNER,
                // Users
                (GET    to "/users/{username}") to ROLE_BEGINNER,
                (GET    to "/users/{username}/reports") to ROLE_BEGINNER,
                (GET    to "/users/{username}/reports/{reportId}") to ROLE_BEGINNER,
                (POST   to "/users/{username}/reports") to ROLE_BEGINNER,
                (POST   to "/users/{username}/reports/{reportId}") to ROLE_ADMIN,
                (DELETE to "/users/{username}/reports/{reportId}") to ROLE_ADMIN
        )
    }

    fun configureRequestAuthorizations(http: HttpSecurity) {
        // Define role hierarchy
        http.authorizeRequests().expressionHandler(webExpressionHandler())
        // Match roles to URIs
        REPUTATION_MATCHERS.forEach {
            http.authorizeRequests().antMatchers(it.key.first, it.key.second).hasRole(it.value.name.removePrefix("ROLE_"))
        }
    }

    private fun webExpressionHandler(): SecurityExpressionHandler<FilterInvocation> {
        val defaultWebSecurityExpressionHandler = DefaultWebSecurityExpressionHandler()
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy())
        return defaultWebSecurityExpressionHandler
    }

    @Bean
    fun roleHierarchy(): RoleHierarchyImpl {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(ROLE_HIERARCHY)
        return roleHierarchy
    }

}