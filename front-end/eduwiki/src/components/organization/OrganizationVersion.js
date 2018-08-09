import React from 'react'
import fetch from 'isomorphic-fetch'
import Layout from '../layout/Layout'
import {Row, Col, Card, Button, Icon} from 'antd'
import Cookies from 'universal-cookie'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organization: {
        fullName: '',
        shortName: '',
        timestamp: '',
        createdBy: ''
      },
      id: 0,
      version: 0,
      err: undefined,
      full_name: '',
      short_name: '',
      address: '',
      contact: '',
      createdBy: ''
    }
  }
  render () {
    return (
      <Layout>
        <div>
          <div style={{ padding: '20px' }}>
            <div className='title_div'>
              <h1>{this.state.organization.fullName} - {this.state.organization.shortName} <small>({this.state.organization.timestamp})</small></h1>
            </div>
            <div className='version_div'>
              <p>version {this.props.version}</p>
            </div>
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
            <Button type='primary' onClick={() => this.props.history.push('/organization')}>
              <Icon type='left' />Back to actual version
            </Button>
          </div>
        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const url = `http://localhost:8080/organizations/${this.props.match.params.id}/versions/${this.props.match.params.version}`
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
        organization: json,
        id: json.organizationId,
        version: json.version
      }))
      .catch(err => this.setState({err: err}))
  }
}
