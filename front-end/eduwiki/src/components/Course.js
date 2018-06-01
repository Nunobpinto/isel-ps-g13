import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      votes: 0,
      timestamp: '',
      terms: [],
      exams: [],
      workAssignments: [],
      voteType: undefined,
      examFlag: false,
      workAssignmentFlag: false,
      termId: undefined,
      courseError: undefined,
      termError: undefined,
      examError: undefined,
      workAssignmentError: undefined
    }
    this.getExams = this.getExams.bind(this)
    this.getWorkAssignments = this.getWorkAssignments.bind(this)
    this.fetchExams = this.fetchExams.bind(this)
    this.fetchWorkAssignments = this.fetchWorkAssignments.bind(this)
  }

  getExams (termId) {
    this.setState({
      examFlag: true,
      termId: termId
    })
  }

  getWorkAssignments (termId) {
    this.setState({
      workAssignmentFlag: true,
      termId: termId
    })
  }

  render () {
    return (
      <div>
        {this.state.courseError
          ? <p> Error getting this course, please try again !!! </p>
          : <div>
            <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small> </h1>
            <div>
              <p>Created By : {this.state.createdBy}</p>
              <p>Votes : {this.state.votes}</p>
              {this.state.termError
                ? <p>this.state.termError </p>
                : <ul>
                  {this.state.terms.map(item =>
                    <div>
                      <li key={item.id}>
                        {item.shortName}
                      </li>
                      <button id={`exam_button_term${item.id}`} key={item.id} onClick={() => this.getExams(item.id)}>Exams</button>
                      {this.state.exams.map(exam => {
                        if (exam.termId === item.id) {
                          return (
                            <li key={exam.id}>
                              {exam.sheet}
                            </li>
                          )
                        }
                      }
                      )}
                      <button id={`wrs_button_term${item.id}`} key={item.id} onClick={() => this.getWorkAssignments(item.id)}>WorkAssignments</button>
                      {this.state.workAssignments.map(wrs => {
                        if (wrs.termId === item.id) {
                          return (
                            <li key={wrs.id}>
                              {wrs.sheet}
                            </li>
                          )
                        }
                      }
                      )}
                    </div>
                  )}
                </ul>
              }
            </div>
          </div>
        }
      </div>
    )
  }

  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id
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
        const termsUri = `http://localhost:8080/courses/${json.id}/terms`
        fetch(termsUri, header)
          .then(resp => {
            if (resp.status >= 400) {
              throw new Error('Unable to access content')
            }
            const ct = resp.headers.get('content-type') || ''
            if (ct === 'application/json' || ct.startsWith('application/json;')) {
              return resp.json()
            }
            throw new Error(`unexpected content type ${ct}`)
          })
          .then(terms =>
            this.setState({
              full_name: json.fullName,
              short_name: json.shortName,
              createdBy: json.createdBy,
              timestamp: json.timestamp,
              votes: json.votes,
              terms: terms
            }))
          .catch(err => {
            this.setState({termError: err})
          })
      })
      .catch(error => {
        this.setState({
          courseError: error
        })
      })
  }

  componentDidUpdate () {
    if (this.state.examFlag) {
      const courseId = this.props.match.params.id
      this.fetchExams(courseId, this.state.termId)
    } else if (this.state.workAssignmentFlag) {
      const courseId = this.props.match.params.id
      this.fetchWorkAssignments(courseId, this.state.termId)
    }
  }

  fetchExams (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/exams`
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
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(exams => this.setState(
        {
          examFlag: false,
          exams: exams
        }
      ))
      .catch(error => this.setState(
        {
          examFlag: false,
          examError: error
        }
      ))
  }

  fetchWorkAssignments (courseId, termId) {
    const uri = `http://localhost:8080/courses/${courseId}/terms/${termId}/workAssignments`
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
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(works => this.setState(
        {
          workAssignmentFlag: false,
          workAssignments: works
        }
      ))
      .catch(error => this.setState(
        {
          workAssignmentFlag: false,
          workAssignmentError: error
        }
      ))
  }
}
