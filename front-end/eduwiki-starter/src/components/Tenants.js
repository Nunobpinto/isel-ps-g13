import React from 'react'
import fetch from 'isomorphic-fetch'
import { message, List, Button } from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      tenants: [],
      pending: [],
      loadingTenants: true,
      loadingPending: false
    }
  }
  render () {
    return (
      <div>
        <h2>All existing Tenants</h2>
        <List
          bordered
          loading={this.state.loadingTenants}
          dataSource={this.state.tenants}
          renderItem={item => (
            <List.Item>
              {item.schemaName.toUpperCase()} created at {item.createdAt}
            </List.Item>)}
        />
        <Button onClick={() => this.setState({seePending: true})}>Add other Tenant</Button>
        {this.state.showPendingList &&
        <List
          header={<div>Pending Tenants Waiting to be Approved</div>}
          footer={<div><Button onClick={() => this.props.history.push('/create-tenant')}>Petition to create Tenant</Button></div>}
          bordered
          dataSource={this.state.pending}
          loading={this.state.loadingPending}
          renderItem={item => (
            <List.Item>
              <p>{item.fullName} ({item.shortName})</p>
              <p>{item.address}</p>
              <p>{item.contact}</p>
              <p>{item.website}</p>
              <p>{item.orgSummary}</p>
            </List.Item>)}
        />
        }
      </div>
    )
  }
  componentDidMount () {
    const uri = 'http://localhost:8080/tenants'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*'
      }
    }
    fetch(uri, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error()
        }
        return resp.json()
      })
      .then(json => this.setState({
        tenants: json.tenantList,
        loadingTenants: false
      }))
      .catch(() => message.error('Error fetching tenants'))
  }
  componentDidUpdate () {
    if (this.state.seePending) {
      const uri = 'http://localhost:8080/tenants/pending'
      const options = {
        headers: {
          'Access-Control-Allow-Origin': '*'
        }
      }
      fetch(uri, options)
        .then(resp => {
          if (resp.status >= 400) {
            throw new Error()
          }
          return resp.json()
        })
        .then(json => this.setState({
          pending: json.pendingTenantList,
          loadingPending: false,
          seePending: false,
          showPendingList: true
        }))
        .catch(() => message.error('Error fetching pending tenants'))
    }
  }
}
