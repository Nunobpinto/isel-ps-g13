import React from 'react'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import {Button, Icon, message} from 'antd'
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
              <p>version {this.props.match.params.version}</p>
            </div>
            <p>Created By: {this.state.createdBy}</p>
            <Button type='primary' onClick={() => this.props.history.push(`/courses/${this.props.match.params.id}`)}>
              <Icon type='left' />Back to actual version
            </Button>
          </div>
        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const url = `http://localhost:8080/courses/${this.props.match.params.id}/versions/${this.props.match.params.version}`
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
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
