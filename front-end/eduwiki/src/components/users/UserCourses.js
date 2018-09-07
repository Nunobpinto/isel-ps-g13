import React from 'react'
import fetcher from '../../fetcher'
import {Card} from 'antd'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      courses: [],
      error: 'Follow a course',
      loadingCourses: true
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My Courses'
          loading={this.state.loadingCourses}
          actions={[
            <p onClick={() => this.props.history.push('/courses')}>
                    See all courses
            </p>
          ]}
        >
          {this.state.courses.length === 0
            ? <p>Try following a course or try later</p>
            : <ul>
              {this.state.courses.map(course => (
                <a href={`/courses/${course.courseId}`}>
                  <li>{course.shortName}</li>
                </a>
              ))}
            </ul>
          }

        </Card>
      </div>
    )
  }
  componentDidMount () {
    const authCookie = this.props.auth
    const options = {
      headers: {
        'Authorization': 'Basic ' + authCookie,
        'Access-Control-Allow-Origin': '*',
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/user/courses', options)
      .then(json => this.setState({
        courses: json.courseList,
        error: undefined,
        loadingCourses: false
      }))
      .catch(_ => this.setState({
        error: 'Try following a course or try later',
        loadingCourses: false
      }))
  }
}
