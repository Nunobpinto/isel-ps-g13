import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'
import Layout from './Layout'
import ReportProgramme from './ReportProgramme'
import ProgrammeReports from './ProgrammeReports'
import {Row, Col, Card, Button, Tooltip, Popover} from 'antd'

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
      voteUp: false,
      voteDown: false,
      progError: undefined,
      courseError: undefined
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
  }

  render () {
    return (
      <Layout>
        <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small> </h1>
        <p>
          Votes : {this.state.votes}
          <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
            <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
          </Tooltip>
          <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
            <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
          </Tooltip>
          <Popover content={<ReportProgramme id={this.props.match.params.id} />} trigger='click'>
            <Tooltip placement='bottom' title='Report this Programme'>
              <Button id='report_btn' shape='circle' icon='warning' />
            </Tooltip>
          </Popover>
          <Popover placement='bottomLeft' content={<ProgrammeReports id={this.props.match.params.id} />} trigger='click'>
            <Button type='primary' id='show_reports_btn'>
              Show all Reports On This Programme
            </Button>
          </Popover>

        </p>
        <div style={{ padding: '20px' }}>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Academic Degree'>
                {this.state.academic_degree}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Total Credits'>
                {this.state.total_credits}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Duration'>
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
                  <Link to={{pathname: `/courses/${item.id}`}}>See it's page</Link>
                </Card>
              </Col>
            )}
          </Row>
        </div>
      </Layout>
    )
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(uri, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => ({
          voteUp: false,
          votes: prevState.votes + 1
        }))
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down',
      created_by: 'ze'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/programmes/' + id + '/vote'
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(voteInput)
    }
    fetch(uri, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => ({
          voteDown: false,
          votes: prevState.votes - 1
        }))
      })
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

  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }
}
