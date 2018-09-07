import React from 'react'
import fetcher from '../fetcher'
import { message, List, Card, Row, Col, Button, Icon, Spin } from 'antd'
import Layout from './Layout'
import config from '../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      pending: {},
      loading: true
    }
    this.approve = this.approve.bind(this)
    this.reject = this.reject.bind(this)
  }
  approve () {
    const uri = config.API_PATH + '/tenants/pending/' + this.props.match.params.tenantId
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, options)
      .then(_ => {
        message.success('Approved Tenant')
        this.props.history.push('/admin')
        this.setState({
          approve: false
        })
      })
      .catch(err => {
        if (err.detail) {
          message.error(err.detail)
        } else {
          message.error('Error approving tenant')
        }
        this.setState({
          approve: false
        })
      })
  }

  reject () {
    const uri = config.API_PATH + '/tenants/pending/' + this.props.match.params.tenantId
    const options = {
      method: 'DELETE',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, options)
      .then(_ => {
        message.success('Rejected Tenant')
        this.props.history.push('/admin')
        this.setState({
          reject: false
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error rejecting tenant')
        }
        this.setState({
          reject: false
        })
      })
  }
  render () {
    return (
      <Layout history={this.props.history} auth>
        <h2>{this.state.pending.fullName} - {this.state.pending.timestamp}</h2>
        {this.state.approve && <Spin tip='Submitting the Approval of this tenant' />}
        {this.state.reject && <Spin tip='Submitting the Reject of this tenant' />}
        <Row gutter={16}>
          <Col span={8}>
            <Card title='Address' loading={this.state.loading}>
              {this.state.pending.address}
            </Card>
          </Col>
          <Col span={8}>
            <Card title='Contact' loading={this.state.loading}>
              {this.state.pending.contact}
            </Card>
          </Col>
          <Col span={8}>
            <Card title='Website' loading={this.state.loading}>
              {this.state.pending.website}
            </Card>
          </Col>
          <Col span={8}>
            <Card title='Email Pattern' loading={this.state.loading}>
              {this.state.pending.emailPattern}
            </Card>
          </Col>
          <Col span={8}>
            <Card title='Summary' loading={this.state.loading}>
              {this.state.pending.orgSummary}
            </Card>
          </Col>
        </Row>
        <List
          loading={this.state.loading}
          header={<h1>List of Creators</h1>}
          dataSource={this.state.pending.creators}
          grid={{ gutter: 16, column: 4 }}
          renderItem={item => (
            <List.Item>
              <Card
                title={item.username}
              >
                <p>{item.email}</p>
                <p>{item.givenName}</p>
                <p>{item.familyName}</p>
                {item.isPrincipal && <p>Principal User</p>}
              </Card>
            </List.Item>
          )}
        />

        <Button type='primary' onClick={() => this.setState({approve: true})}>
          <Icon type='check' />Approve Tenant
        </Button>
        <br />
        <Button type='danger' onClick={() => this.setState({reject: true})}>
          <Icon type='close' />Reject Tenant
        </Button>

      </Layout>
    )
  }
  componentDidUpdate () {
    if (this.state.approve) {
      this.approve()
    } else if (this.state.reject) {
      this.reject()
    }
  }
  componentDidMount () {
    const uri = config.API_PATH + '/tenants/pending/' + this.props.match.params.tenantId
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(uri, options)
      .then(json => this.setState({
        pending: json,
        loading: false
      }))
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Error fetching tenant')
        }
        this.setState({
          loading: false
        })
      })
  }
}
