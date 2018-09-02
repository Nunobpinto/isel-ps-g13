import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import ReportClass from './ReportClass'
import ClassVersions from './ClassVersions'
import Layout from '../layout/Layout'
import { Button, Row, Col, message, Tooltip, Card, Popover } from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      klass: {},
      classCourses: []
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
  }
  render () {
    return (
      <Layout>
        <div className='title_div'>
          <h1>
            <strong>Class {this.state.klass.lecturedTerm}</strong>
             /
            <strong>{this.state.klass.programmeShortName}</strong>
             /
            <strong>{this.state.klass.className} - ({this.state.klass.timestamp})</strong>
          </h1>
        </div>
        <div className='version_div'>
          <Popover placement='bottom' content={<ClassVersions auth={cookies.get('auth')} classId={this.props.match.params.classId} version={this.state.klass.version} />} trigger='click'>
            <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.klass.version}
            </Button>
          </Popover>
        </div>
        <div>
          <p>Created By : {this.state.klass.createdBy}</p>
          <p>
                  Votes : {this.state.klass.votes}
            <Tooltip placement='bottom' title={`Vote Up on ${this.state.klass.className}`}>
              <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
            </Tooltip>
            <Tooltip placement='bottom' title={`Vote Down on ${this.state.klass.className}`}>
              <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
            </Tooltip>
            <Popover content={<ReportClass classId={this.props.match.params.classId} programmeId={this.state.klass.programmeId} />} trigger='click'>
              <Tooltip placement='bottom' title='Report this Class'>
                <Button id='report_btn' shape='circle' icon='warning' />
              </Tooltip>
            </Popover>
            <a href={`/classes/${this.props.match.params.classId}/reports`}>
              <Button type='primary' id='show_reports_btn'>
              Show all Reports On This Class
              </Button>
            </a>
          </p>
        </div>
        <div style={{ padding: '20px' }}>
          <h1>Courses</h1>
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              {this.state.classCourses.map(item =>
                <Col span={8} key={item.courseId}>
                  <Card title={item.courseShortName}>
                    <p>Created by {item.createdBy}</p>
                    <p>Votes : {item.votes}</p>
                    <p>Added at {item.timestamp}</p>
                    <Link to={{pathname: `/classes/${item.classId}/courses/${item.courseId}`}}>See it's page</Link>
                  </Card>
                </Col>
              )}
            </Row>
          </div>
        </div>
        <div>
            Add courses to this class div
        </div>
      </Layout>
    )
  }
  voteUp () {
    const voteInput = {
      vote: 'Up'
    }
    const url = `http://localhost:8080/classes/${this.props.match.params.classId}/vote`
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
    fetcher(url, body)
      .then(_ => {
        message.success('Voted up!!!')
        this.setState(prevState => {
          prevState.klass.votes += 1
          return ({
            klass: prevState.klass,
            voteUp: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteUp: false})
      })
  }
  voteDown () {
    const voteInput = {
      vote: 'Down'
    }
    const url = `http://localhost:8080/classes/${this.props.match.params.classId}/vote`
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
    fetcher(url, body)
      .then(_ => {
        message.success('Voted up!!!')
        this.setState(prevState => {
          prevState.klass.votes -= 1
          return ({
            klass: prevState.klass,
            voteDown: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote down')
        }
        this.setState({voteDown: false})
      })
  }
  componentDidUpdate () {
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }
  componentDidMount () {
    const id = this.props.match.params.classId
    const uri = 'http://localhost:8080/classes/' + id
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(klass => this.setState({klass: klass}))
      .catch(error => {
        message.error('Error fetching class with id ' + id)
        this.setState({error: error})
      })
  }
}
