import React from 'react'
import fetcher from '../../fetcher'
import {Card} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      courses: [],
      error: 'Follow a course'
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My Courses'
          actions={[
            <p onClick={() => this.props.history.push('/courses')}>
                    See all courses
            </p>
          ]}
        >
          {this.state.error
            ? <p>{this.state.error}</p>
            : <ul>
              {this.state.courses.map(course => (
                <li>{course.fullName} ({course.shortName})</li>
              ))}
            </ul>}
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
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user/courses', options)
      .then(json => this.setState({
        courses: json.courseList,
        error: undefined
      }))
      .catch(_ => this.setState({error: 'Try following a course or try later'}))
  }
}
