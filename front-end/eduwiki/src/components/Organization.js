import React from 'react'
import fetch from 'isomorphic-fetch'
import Navbar from './Navbar'
import {Row, Col, Card, Button, Tooltip} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organization: undefined,
      err: undefined,
      full_name: '',
      short_name: '',
      address: '',
      contact: '',
      createdBy: '',
      voteUp: false,
      voteDown: false,
      redirect: false
    }
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit (ev) {
    ev.preventDefault()
    this.setState({redirect: true})
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const id = this.state.organization.id
    const uri = 'http://localhost:8080/organizations/' + id + '/vote'
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
    const id = this.state.organization.id
    const uri = 'http://localhost:8080/organizations/' + id + '/vote'
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

  render () {
    return (
      <div>
        <div>
          <Navbar />
        </div>
        <div>
          {this.state.organization
            ? <div style={{ padding: '20px' }}>
              <h1> {this.state.organization.fullName} - ({this.state.organization.shortName}) </h1>
              <p>
                Votes : {this.state.votes}
                <Tooltip placement='bottom' title={`Vote Up on ${this.state.organization.shortName}`}>
                  <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
                </Tooltip>
                <Tooltip placement='bottom' title={`Vote Down on ${this.state.organization.shortName}`}>
                  <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
                </Tooltip>
              </p>
              <p>Created By: {this.state.organization.createdBy}</p>
              <Row gutter={16}>
                <Col span={5}>
                  <Card title='Address'>
                    {this.state.organization.address}
                  </Card>
                </Col>
                <Col span={5}>
                  <Card title='Contact'>
                    {this.state.organization.contact}
                  </Card>
                </Col>
              </Row>
            </div>
            : <div>
              <form onSubmit={this.handleSubmit}>
                <input type='text' name='full_name' onChange={this.handleChange} />
                <input type='text' name='short_name' onChange={this.handleChange} />
                <input type='text' name='address' onChange={this.handleChange} />
                <input type='text' name='contact' onChange={this.handleChange} />
                <input type='text' name='createdBy' onChange={this.handleChange} />
                <input type='submit' value='Submit' />
              </form>
            </div>
          }
        </div>
      </div>
    )
  }

  componentDidMount () {
    const url = 'http://localhost:8080/organizations'
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*'
      }
    }
    fetch(url, headers)
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
      .then(json => this.setState({
        organization: json[0],
        votes: json[0].votes
      }))
      .catch(err => this.setState({err: err}))
  }

  componentDidUpdate () {
    if (this.state.redirect) {
      const data = {
        full_name: this.state.full_name,
        short_name: this.state.short_name,
        address: this.state.address,
        contact: this.state.contact,
        createdBy: this.state.createdBy
      }
      const body = {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      }
      const url = 'http://localhost:8080/organizations'
      this.promise = fetch(url, body)
        .then(resp => {
          if (resp.status >= 400) {
            throw new Error('Unable to access content')
          }
          this.setState({redirect: true})
        })
        .catch(error => {
          this.setState({
            redirect: false,
            error: error
          })
        })
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDown) {
      this.voteDown()
    }
  }
}
