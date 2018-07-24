import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'
import MyLayout from './Layout'
import Term from './Term'
import {Button, Card, Col, Row, Tooltip, Menu, Layout} from 'antd'

const { Content, Sider } = Layout
const { SubMenu } = Menu

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
      classes: [],
      workAssignments: [],
      voteType: undefined,
      examFlag: false,
      workAssignmentFlag: false,
      termId: undefined,
      courseError: undefined,
      termError: undefined,
      examError: undefined,
      workAssignmentError: undefined,
      voteUp: false,
      voteDown: false
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.showTerm = this.showTerm.bind(this)
  }

  showTerm (term) {
    this.setState({term: term})
  }

  render () {
    return (
      <div>
        <MyLayout>
          {this.state.courseError
            ? <p> Error getting this course, please try again !!! </p>
            : <div>
              <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small> </h1>
              <div>
                <p>Created By : {this.state.createdBy}</p>
                <p>
                  Votes : {this.state.votes}
                  <Tooltip placement='bottom' title={`Vote Up on ${this.state.short_name}`}>
                    <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
                  </Tooltip>
                  <Tooltip placement='bottom' title={`Vote Down on ${this.state.short_name}`}>
                    <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
                  </Tooltip>
                </p>
                {this.state.termError
                  ? <p>this.state.termError </p>
                  : <Layout style={{ padding: '24px 0', background: '#fff' }}>
                    <Sider width={200} style={{ background: '#fff' }}>
                      <Menu
                        mode='inline'
                        style={{ height: '100%' }}
                      >
                        {this.state.terms.map(item =>
                          <Menu.Item
                            key={item.id}
                            onClick={() => this.showTerm(item)}
                          >
                            {item.shortName}
                          </Menu.Item>

                        )}
                      </Menu>
                    </Sider>
                    <Content style={{ padding: '0 24px', minHeight: 280 }}>
                      {this.state.term
                        ? <Term term={this.state.term} courseId={this.props.match.params.id} />
                        : <h1>Please choose one of the available Terms</h1>
                      }
                    </Content>
                  </Layout>
                }

              </div>
            </div>
          }
        </MyLayout>
      </div>
    )
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id + '/vote'
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
    const uri = 'http://localhost:8080/courses/' + id + '/vote'
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
        const termsUri = `http://localhost:8080/courses/${json.courseId}/terms`
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
    if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }
}
