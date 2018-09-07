import React from 'react'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import { Button, Icon, message } from 'antd'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      klass: {}
    }
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
            <strong>{this.state.klass.className} - ({timestampParser(this.state.klass.timestamp)})</strong>
          </h1>
        </div>
        <div className='version_div'>
          <h1> Version {this.state.klass.version}</h1>
        </div>
        <div>
          <p>Created By : <a href={`/users/${this.state.klass.createdBy}`}>{this.state.klass.createdBy}</a></p>
          <Button type='primary' onClick={() => this.props.history.push(`/classes/${this.props.match.params.classId}`)}>
            <Icon type='left' />Back to actual version
          </Button>
        </div>
      </Layout>
    )
  }
  componentDidMount () {
    const id = this.props.match.params.classId
    const version = this.props.match.params.version
    const uri = config.API_PATH + '/classes/' + id + '/versions/' + version
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, header)
      .then(klass => this.setState({klass: klass}))
      .catch(error => {
        message.error('Error fetching class version')
        this.setState({error: error})
      })
  }
}
