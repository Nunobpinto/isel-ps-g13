import React from 'react'
import fetcher from '../../fetcher'
import Layout from '../layout/Layout'
import {Row, Col, Card, Button, Icon, message} from 'antd'
import timestampParser from '../../timestampParser'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organization: {
        fullName: '',
        shortName: '',
        timestamp: '',
        createdBy: '',
        website: ''
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
              <h1>{this.state.organization.fullName} - {this.state.organization.shortName} <small>({timestampParser(this.state.organization.timestamp)})</small></h1>
            </div>
            <div className='version_div'>
              <p>version {this.props.match.params.version}</p>
            </div>
            <p>Created By: <a href={`/users/${this.state.organization.createdBy}`}>{this.state.organization.createdBy}</a></p>
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
              <Col span={5}>
                <Card title='Website'>
                  {this.state.organization.website}
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
    const url = `${config.API_PATH}/organization/versions/${this.props.match.params.version}`
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, headers)
      .then(json => this.setState({
        organization: json,
        version: json.version
      }))
      .catch(err => {
        message.error(err.detail)
        this.setState({err: err})
      })
  }
}
