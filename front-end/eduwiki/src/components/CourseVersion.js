import React from 'react'
import fetch from 'isomorphic-fetch'
import Layout from './Layout'
import {Button, Icon} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      version: 0,
      timestamp: ''
    }
  }
  render () {
    return (
      <Layout>
        <div>
          <div style={{ padding: '20px' }}>
            <div className='title_div'>
              <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small></h1>
            </div>
            <div className='version_div'>
              <p>version {this.props.version}</p>
            </div>
            <p>Created By: {this.state.createdBy}</p>
            <Button type='primary' onClick={() => this.props.history.push(`/courses/${this.props.match.params.courseId}`)}>
              <Icon type='left' />Back to actual version
            </Button>
          </div>
        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const url = `http://localhost:8080/courses/${this.props.match.params.courseId}/versions/${this.props.match.params.version}`
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
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
        full_name: json.fullName,
        short_name: json.shortName,
        createdBy: json.createdBy,
        timestamp: json.timestamp,
        version: json.version,
        id: json.courseId
      }))
      .catch(err => this.setState({err: err}))
  }
}
