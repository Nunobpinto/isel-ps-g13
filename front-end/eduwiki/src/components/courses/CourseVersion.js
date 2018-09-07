import React from 'react'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import {Button, Icon, message} from 'antd'
import timestampParser from '../../timestampParser'
import config from '../../config'

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
              <h1>{this.state.full_name} - {this.state.short_name} <small>({timestampParser(this.state.timestamp)})</small></h1>
            </div>
            <div className='version_div'>
              <p>version {this.props.match.params.version}</p>
            </div>
            <p>Created By: <a href={`/users/${this.state.createdBy}`}>{this.state.createdBy}</a></p>
            <Button type='primary' onClick={() => this.props.history.push(`/courses/${this.props.match.params.id}`)}>
              <Icon type='left' />Back to actual version
            </Button>
          </div>
        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const url = `${config.API_PATH}/courses/${this.props.match.params.id}/versions/${this.props.match.params.version}`
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(url, headers)
      .then(json => this.setState({
        full_name: json.fullName,
        short_name: json.shortName,
        createdBy: json.createdBy,
        timestamp: json.timestamp,
        version: json.version,
        id: json.courseId
      }))
      .catch(error => {
        message.error('Error fetching version of course')
        this.setState({err: error})
      })
  }
}
