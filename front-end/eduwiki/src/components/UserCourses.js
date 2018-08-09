import React from 'react'
import fetch from 'isomorphic-fetch'
import {Card, message} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      courses: []
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
          <ul>
            {this.state.courses.map(course => (
              <li>{course.fullName} ({course.shortName})</li>
            ))}
          </ul>
        </Card>
      </div>
    )
  }
  componentDidMount () {
    const authCookie = this.props.auth
    const options = {
      headers: {
        'Authorization': 'Basic ' + authCookie,
        'Access-Control-Allow-Origin': '*'
      }
    }
    fetch('http://localhost:8080/user/courses', options)
      .then(resp => {
        if (resp.status >= 400) throw new Error('Error!!!')
        return resp.json()
      })
      .then(json => this.setState({courses: json}))
      .catch(_ => message.error('Something bad happened, please try again'))
  }
}
