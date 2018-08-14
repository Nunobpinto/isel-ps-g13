package isel.leic.ps.eduWikiAPI.service.interfaces

import isel.leic.ps.eduWikiAPI.domain.inputModel.*
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.CourseProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.inputModel.reports.ProgrammeReportInputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.CourseProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.ProgrammeOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.CourseProgrammeCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.ProgrammeCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.CourseProgrammeReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.reports.ProgrammeReportCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.CourseProgrammeStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.staging.ProgrammeStageCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.CourseProgrammeVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.collections.version.ProgrammeVersionCollectionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.CourseProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.reports.ProgrammeReportOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.CourseProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.staging.ProgrammeStageOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.CourseProgrammeVersionOutputModel
import isel.leic.ps.eduWikiAPI.domain.outputModel.single.version.ProgrammeVersionOutputModel
import java.security.Principal

interface ProgrammeService {

    fun getAllProgrammes(): ProgrammeCollectionOutputModel

    fun getSpecificProgramme(programmeId: Int): ProgrammeOutputModel

    fun createProgramme(inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeOutputModel

    fun createStagingProgramme(inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeStageOutputModel

    fun getSpecificStageEntryOfProgramme(stageId: Int) : ProgrammeStageOutputModel

    fun createProgrammeFromStaged(stageId: Int, principal: Principal): ProgrammeOutputModel

    fun getAllProgrammeStageEntries() : ProgrammeStageCollectionOutputModel

    fun getAllReportsOfSpecificProgramme(programmeId: Int): ProgrammeReportCollectionOutputModel

    fun getSpecificReportOfProgramme(programmeId: Int, reportId: Int): ProgrammeReportOutputModel

    fun getAllCoursesOnSpecificProgramme(programmeId: Int): CourseProgrammeCollectionOutputModel

    fun addCourseToProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, principal: Principal): CourseProgrammeOutputModel

    fun voteOnProgramme(programmeId: Int, vote: VoteInputModel, principal: Principal): Int

    fun reportProgramme(programmeId: Int, inputProgrammeReport: ProgrammeReportInputModel, principal: Principal): ProgrammeReportOutputModel

    fun voteOnReportedProgramme(programmeId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun updateProgrammeFromReport(programmeId: Int, reportId: Int, principal: Principal): ProgrammeOutputModel

    fun voteOnStagedProgramme(stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun deleteSpecificProgramme(programmeId: Int, principal: Principal): Int

    fun deleteSpecificStagedProgramme(stageId: Int, principal: Principal): Int

    fun deleteSpecificReportOnProgramme(programmeId: Int, reportId: Int, principal: Principal) : Int

    fun partialUpdateOnProgramme(programmeId: Int, inputProgramme: ProgrammeInputModel, principal: Principal): ProgrammeOutputModel

    fun getAllVersionsOfProgramme(programmeId: Int): ProgrammeVersionCollectionOutputModel

    fun getSpecificVersionOfProgramme(programmeId: Int, version: Int): ProgrammeVersionOutputModel

    fun reportSpecificCourseOnProgramme(programmeId: Int, courseId: Int, inputCourseReport: CourseProgrammeReportInputModel, principal: Principal): CourseProgrammeReportOutputModel

    fun getSpecificCourseOfProgramme(programmeId: Int, courseId: Int): CourseProgrammeOutputModel

    fun getAllVersionsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeVersionCollectionOutputModel

    fun getSpecificVersionOfCourseOnProgramme(programmeId: Int, courseId: Int, version: Int): CourseProgrammeVersionOutputModel

    fun getAllReportsOfCourseOnProgramme(programmeId: Int, courseId: Int): CourseProgrammeReportCollectionOutputModel

    fun getSpecificReportOfCourseOnProgramme(programmeId: Int, courseId: Int, reportId: Int): CourseProgrammeReportOutputModel

    fun voteOnCourseProgramme(programmeId: Int, courseId: Int, vote: VoteInputModel, principal: Principal): Int

    fun createStagingCourseOnProgramme(programmeId: Int, inputCourseProgramme: CourseProgrammeInputModel, principal: Principal): CourseProgrammeStageOutputModel

    fun createCourseProgrammeFromStaged(programmeId: Int, stageId: Int, principal: Principal): CourseProgrammeOutputModel

    fun voteOnStagedCourseProgramme(programmeId: Int, stageId: Int, vote: VoteInputModel, principal: Principal): Int

    fun voteOnReportedCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, vote: VoteInputModel, principal: Principal): Int

    fun getAllCourseStageEntriesOfSpecificProgramme(programmeId: Int): CourseProgrammeStageCollectionOutputModel

    fun getSpecificStagedCourseOfProgramme(programmeId: Int, stageId: Int): CourseProgrammeStageOutputModel

    fun updateCourseProgrammeFromReport(programmeId: Int, courseId: Int, reportId: Int, principal: Principal): CourseProgrammeOutputModel

    fun deleteSpecificCourseProgramme(programmeId: Int, courseId: Int, principal: Principal): Int

    fun deleteSpecificReportOfCourseProgramme(programmeId: Int, courseId: Int, reportId: Int, principal: Principal): Int

    fun deleteSpecificStagedCourseProgramme(programmeId: Int, stageId: Int, principal: Principal): Int
}