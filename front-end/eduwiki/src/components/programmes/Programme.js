import React from 'react'
import fetcher from '../../fetcher'
import {Link} from 'react-router-dom'
import Layout from '../layout/Layout'
import ReportProgramme from './ReportProgramme'
import IconText from '../comms/IconText'
import ProgrammeVersions from './ProgrammeVersions'
import CourseProgramme from '../courses/CourseProgramme'
import {Row, Col, Card, Button, Tooltip, Popover, message} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

class Programme extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programmeId: 0,
      full_name: '',
      short_name: '',
      academic_degree: '',
      total_credits: 0,
      duration: 0,
      createdBy: '',
      votes: 0,
      timestamp: '',
      versionNumber: '',
      courses: [],
      staged: [],
      allCourses: [],
      versions: [],
      voteUp: false,
      voteDown: false,
      progError: undefined,
      courseError: undefined,
      userFollowing: false,
      unFollowProgrammeFlag: false,
      followProgrammeFlag: false,
      canBeFollowed: false,
      loadingProgramme: true
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.fetchOtherCourses = this.fetchOtherCourses.bind(this)
    this.fetchAllStaged = this.fetchAllStaged.bind(this)
    this.addCourseToProgramme = this.addCourseToProgramme.bind(this)
    this.followProgramme = this.followProgramme.bind(this)
    this.unFollowProgramme = this.unFollowProgramme.bind(this)
    this.addStagedCourseToProgramme = this.addStagedCourseToProgramme.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.approveStagedCourseInProgramme = this.approveStagedCourseInProgramme.bind(this)
  }

  render () {
    return (
      <div>
        <div className='title_div'>
          <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small></h1>
        </div>
        <div className='version_div'>
          <Popover placement='bottom' content={<ProgrammeVersions auth={cookies.get('auth')} id={this.props.match.params.id} version={this.state.versionNumber} />} trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.versionNumber}
            </Button>
          </Popover>
        </div>
        <p>Created By : {this.state.createdBy}</p>
        <p>
          Votes : {this.state.votes}
          <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
            <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
            <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
          </Tooltip>
          <Popover content={<ReportProgramme id={this.props.match.params.id} history={this.props.history} />} trigger='click'>
            <Tooltip placement='bottom' title='Report this Programme'>
              <Button id='report_btn' shape='circle' icon='warning' />
            </Tooltip>
          </Popover>
          <Button type='primary' id='show_reports_btn' onClick={() => this.props.history.push(`/programmes/${this.props.match.params.id}/reports`)}>
              Show all Reports On This Programme
          </Button>
          {this.state.canBeFollowed &&
            <Tooltip placement='bottom' title={'Follow this programme'}>
              <Button id='report_btn' icon='heart' onClick={() => this.setState({followProgrammeFlag: true})} />
            </Tooltip>
          }
          {this.state.userFollowing &&
            <Tooltip placement='bottom' title={'Unfollow this programme'}>
              <Button id='report_btn' icon='close-circle' onClick={() => this.setState({unFollowProgrammeFlag: true})} />
            </Tooltip>
          }

        </p>
        <div style={{ padding: '20px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card loading={this.state.loadingProgramme} title='Academic Degree'>
                {this.state.academic_degree}
              </Card>
            </Col>
            <Col span={5}>
              <Card loading={this.state.loadingProgramme} title='Total Credits'>
                {this.state.total_credits}
              </Card>
            </Col>
            <Col span={5}>
              <Card loading={this.state.loadingProgramme} title='Duration'>
                {this.state.duration}
              </Card>
            </Col>
          </Row>
        </div>
        <h1>Courses</h1>
        <div style={{ padding: '30px' }}>
          <Row gutter={16}>
            {this.state.courses.map(item =>
              <Col span={8} key={item.id}>
                <Card title={item.shortName}>
                  <p>{item.fullName} - <small>{item.createdBy}</small> </p>
                  {item.optional === false ? <p>Mandatory</p> : <p>Optional</p>}
                  <p>Credits : {item.credits}</p>
                  <p>Lectured in term {item.lecturedTerm} </p>
                  <Link to={{pathname: `/courses/${item.courseId}`}}>See it's page</Link>
                </Card>
              </Col>
            )}
          </Row>
        </div>
        <div>
          <Button onClick={() => this.setState({seeAllCourses: true, seeAllStaged: true})}>Add Course To This Programme</Button>
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              <p>Already staged Courses In Programme</p>
              {this.state.staged.map(staged =>
                <Col span={8} key={staged.stageId}>
                  <Card title={staged.courseShortName}>
                    <p>Lectured in {staged.lecturedTerm}</p>
                    <p>Optional : {staged.optional ? 'Yes' : 'No'}</p>
                    <p>Credits: {staged.credits}</p>
                    <p>Votes: {staged.votes}</p>
                    <p>Created By {staged.createdBy}</p>
                    <p>{staged.timestamp}</p>
                    <IconText
                      type='like-o'
                      id='like_btn'
                      onClick={() =>
                        this.setState({
                          voteStagedCoursesUp: true,
                          stageId: staged.stagedId
                        })}
                    />
                    <IconText
                      type='dislike-o'
                      id='dislike_btn'
                      onClick={() =>
                        this.setState({
                          voteStagedCoursesDown: true,
                          stageId: staged.stagedId
                        })}
                    />
                    {
                      this.props.user.reputation.role === 'ROLE_ADMIN' &&
                      <div>
                        <IconText
                          type='check-circle'
                          id='like_btn'
                          text='Approve Staged'
                          onClick={() =>
                            this.setState({
                              approved: true,
                              stageId: staged.stagedId
                            })}
                        />
                        <IconText
                          type='close-circle'
                          id='dislike_btn'
                          text='Reject Staged'
                          onClick={() =>
                            this.setState({
                              rejected: true,
                              stageId: staged.stagedId
                            })}
                        />
                      </div>
                    }
                  </Card>
                </Col>
              )}
              <p>Courses that aren't already associated with {this.state.short_name}</p>
              {this.state.allCourses.map(crs =>
                <Col span={8} key={crs.courseId}>
                  <Card title={crs.shortName}>
                    <CourseProgramme crs={crs} addCourse={course => this.setState({addCourse: true, course: course})} />
                  </Card>
                </Col>
              )}
            </Row>
          </div>
        </div>
      </div>
    )
  }

  addCourseToProgramme () {
    const id = this.props.match.params.id
    const courseToAdd = this.state.course
    if (this.props.user.reputation.role !== 'ROLE_ADMIN') {
      return this.addStagedCourseToProgramme()
    }
    const url = 'http://localhost:8080/programmes/' + id + '/courses'
    const data = {
      programme_id: this.props.match.params.id,
      course_id: courseToAdd.course_id,
      course_lectured_term: courseToAdd.lectured_term,
      course_optional: courseToAdd.optional,
      course_credits: courseToAdd.credits
    }
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(data)
    }
    fetcher(url, options)
      .then(courseProgramme => {
        this.setState(prevState => {
          let courses = prevState.courses
          courses.push(courseProgramme)
          let otherCourses = prevState.allCourses
          const index = otherCourses.findIndex(course => course.courseId === courseProgramme.courseId)
          otherCourses.splice(index, 1)
          message.success('Added course to programme with success')
          return ({
            addCourse: false,
            courses: courses,
            allCourses: otherCourses
          })
        })
      })
      .catch(_ => {
        message.error('Error while adding course to programme')
        this.setState({addCourse: false})
      })
  }

  addStagedCourseToProgramme () {
    const id = this.props.match.params.id
    const courseToAdd = this.state.course
    const url = 'http://localhost:8080/programmes/' + id + '/courses/stage'
    const data = {
      programme_id: this.props.match.params.id,
      course_id: courseToAdd.course_id,
      course_lectured_term: courseToAdd.lectured_term,
      course_optional: courseToAdd.optional,
      course_credits: courseToAdd.credits
    }
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(data)
    }
    fetcher(url, options)
      .then(courseProgramme => {
        this.setState(prevState => {
          let staged = prevState.staged
          staged.push(courseProgramme)
          message.success('Added staged course to programme with success')
          return ({
            addCourse: false,
            staged: staged
          })
        })
      })
      .catch(_ => {
        message.error('Error while adding course to programme')
        this.setState({addCourse: false})
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Up!!')
        this.setState(prevState => ({
          voteUp: false,
          votes: prevState.votes + 1
        }))
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteUp: false })
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Down!!')
        this.setState(prevState => ({
          voteDown: false,
          votes: prevState.votes - 1
        }))
      })
      .catch(_ => {
        message.error('Error while processing your vote')
        this.setState({ voteDown: false })
      })
  }

  fetchOtherCourses () {
    const uri = 'http://localhost:8080/courses'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(json => this.setState(prevState => {
        let programmeCourses = prevState.courses
        let newCourses = json.courseList.filter(crs => !programmeCourses.some(cr => crs.courseId === cr.courseId))
        return ({
          seeAllCourses: false,
          allCourses: newCourses
        })
      }))
      .catch(_ => message.error('Error fetching other courses'))
  }

  fetchAllStaged () {
    const id = this.props.match.params.id
    const uri = `http://localhost:8080/programmes/${id}/courses/stage`
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(staged => this.setState({
        staged: staged.courseProgrammeStageList,
        seeAllStaged: false
      }))
      .catch(_ => {
        message.error('Error loading staged courses of programme')
      })
  }

  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(programme => {
        const coursesUri = `http://localhost:8080/programmes/${id}/courses`
        fetcher(coursesUri, header)
          .then(courses => {
            const userProgrammeUri = 'http://localhost:8080/user/programme'
            fetcher(userProgrammeUri, header)
              .then(userProgramme => this.setState({
                full_name: programme.fullName,
                short_name: programme.shortName,
                academic_degree: programme.academicDegree,
                total_credits: programme.totalCredits,
                id: programme.programmeId,
                versionNumber: programme.version,
                duration: programme.duration,
                createdBy: programme.createdBy,
                timestamp: programme.timestamp,
                votes: programme.votes,
                progError: undefined,
                courseError: undefined,
                courses: courses.courseProgrammeList,
                canBeFollowed: (userProgramme.programmeId !== programme.programmeId),
                userFollowing: (userProgramme.programmeId === programme.programmeId),
                loadingProgramme: false
              }))
              .catch(_ => this.setState({
                full_name: programme.fullName,
                short_name: programme.shortName,
                academic_degree: programme.academicDegree,
                total_credits: programme.totalCredits,
                id: programme.programmeId,
                versionNumber: programme.version,
                duration: programme.duration,
                createdBy: programme.createdBy,
                timestamp: programme.timestamp,
                votes: programme.votes,
                progError: undefined,
                courseError: undefined,
                courses: courses.courseProgrammeList,
                canBeFollowed: true,
                loadingProgramme: false
              }))
          })
          .catch(err => {
            message.error('Error loading courses of programme')
            this.setState({
              courseError: err,
              loadingProgramme: false})
          })
      })
      .catch(error => {
        message.error('Error loading programme')
        this.setState({
          progError: error,
          loadingProgramme: false})
      })
  }

  unFollowProgramme () {
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user/programme', options)
      .then(_ => {
        message.success('Unfollowed Programme')
        this.setState({
          userFollowing: false,
          canBeFollowed: true,
          unFollowProgrammeFlag: false
        })
      })
      .catch(_ => {
        message.error('Error while unfollowing Programme')
        this.setState({unFollowProgrammeFlag: false})
      })
  }

  followProgramme () {
    const data = {
      'programme_id': this.state.id
    }
    const options = {
      method: 'POST',
      body: JSON.stringify(data),
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'

      }
    }
    fetcher('http://localhost:8080/user/programme', options)
      .then(_ => {
        message.success('Followed Programme')
        this.setState({
          userFollowing: true,
          canBeFollowed: false,
          followProgrammeFlag: false
        })
      })
      .catch(_ => {
        message.error('Error while following Programme')
        this.setState({followProgrammeFlag: false})
      })
  }

  voteUpStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/courses/stage/' + this.state.stageId + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Up!!')
        this.setState(prevState => {
          let staged = prevState.staged
          const idx = staged.findIndex(st => st.stageId === prevState.stagedId)
          staged[idx].votes += 1
          return ({
            voteStagedCoursesUp: false,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteStagedCoursesUp: false })
      })
  }

  voteDownStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/courses/stage/' + this.state.stageId + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Voted Down!!')
        this.setState(prevState => {
          let staged = prevState.staged
          const idx = staged.findIndex(st => st.stageId === prevState.stagedId)
          staged[idx].votes -= 1
          return ({
            voteStagedCoursesDown: false,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while processing your vote')
        }
        this.setState({ voteStagedCoursesDown: false })
      })
  }

  approveStagedCourseInProgramme () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/courses/stage/' + this.state.stageId
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, body)
      .then(courseProgramme => {
        message.success('Approved Course Programme!!')
        this.setState(prevState => {
          let staged = prevState.staged.filter(st => st.stageId !== prevState.stagedId)
          let courses = prevState.courses
          courses.push(courseProgramme)
          let allCourses = prevState.allCourses
          const idx = allCourses.findIndex(crs => crs.courseId === courseProgramme.courseId)
          delete allCourses[idx]
          return ({
            approved: false,
            courses: courses,
            allCourses: allCourses,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while approving this course programme')
        }
        this.setState({ approved: false })
      })
  }

  rejectStagedCourseInProgramme () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/courses/stage/' + this.state.stageId
    const body = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, body)
      .then(_ => {
        message.success('Rejected staged Course Programme!!')
        this.setState(prevState => {
          let staged = prevState.staged.filter(st => st.stageId !== prevState.stagedId)
          return ({
            rejected: false,
            staged: staged
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error while rejecting this course programme')
        }
        this.setState({ rejected: false })
      })
  }

  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.seeAllCourses) {
      this.fetchOtherCourses()
    } else if (this.state.seeAllStaged) {
      this.fetchAllStaged()
    } else if (this.state.addCourse) {
      this.addCourseToProgramme()
    } else if (this.state.unFollowProgrammeFlag) {
      this.unFollowProgramme()
    } else if (this.state.followProgrammeFlag) {
      this.followProgramme()
    } else if (this.state.voteStagedCoursesUp) {
      this.voteUpStaged()
    } else if (this.state.voteStagedCoursesDown) {
      this.voteDownStaged()
    } else if (this.state.approved) {
      this.approveStagedCourseInProgramme()
    } else if (this.state.rejected) {
      this.rejectStagedCourseInProgramme()
    }
  }
}

export default (props) => (
  <Layout>
    <Programme match={props.match} history={props.history} />
  </Layout>
)
