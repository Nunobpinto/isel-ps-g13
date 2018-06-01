import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      academic_degree: '',
      total_credits: 0,
      duration: 0,
      createdBy: '',
      votes: 0,
      timestamp: '',
      courses: [],
      voteType: undefined,
      progError: undefined,
      courseError: undefined
    }
  }

  render () {
    return (
      <div>
        <div>
          <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small> </h1>
          <button>About</button>
          <div>
            <p>Academic Degree : {this.state.academic_degree}</p>
            <p>Total Credits : {this.state.total_credits}</p>
            <p>Duration : {this.state.duration}</p>
            <p>Created By : {this.state.createdBy}</p>
            <p>Votes : {this.state.votes}</p>
          </div>
          <button>Courses</button>
          <div>
            <ul>
              {this.state.courses.map(item => <li key={item.id}>
                <p>{item.fullName} - {item.createdBy}</p>
              </li>
              )}
            </ul>
          </div>
        </div>
      </div>
    )
  }

  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id
    const header = {
      headers: { 'Access-Control-Allow-Origin': '*' }
    }
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return resp.json().then(json => [resp, json])
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(([resp, json]) => {
        const coursesUri = `http://localhost:8080/programmes/${id}/courses`
        fetch(coursesUri, header)
          .then(response => {
            if (response.status >= 400) {
              throw new Error('Unable to access content')
            }
            const ct = resp.headers.get('content-type') || ''
            if (ct === 'application/json' || ct.startsWith('application/json;')) {
              return response.json()
            }
            throw new Error(`unexpected content type ${ct}`)
          })
          .then(courses =>
            this.setState({
              full_name: json.fullName,
              short_name: json.shortName,
              academic_degree: json.academicDegree,
              total_credits: json.totalCredits,
              duration: json.duration,
              createdBy: json.createdBy,
              timestamp: json.timestamp,
              votes: json.votes,
              progError: undefined,
              courseError: undefined,
              courses: courses
            })
          )
          .catch(err => this.setState({courseError: err}))
      })
      .catch(error => {
        this.setState({
          progError: error
        })
      })
  }
}
