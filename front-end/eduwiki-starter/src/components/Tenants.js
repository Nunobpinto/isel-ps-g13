import React from 'react'
import fetcher from '../fetcher'
import { message, List, Button, Card } from 'antd'
import config from '../config'

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
          grid={{ gutter: 16, column: 4 }}
          header={<div>Pending Tenants Waiting to be Approved</div>}
          footer={<div><Button onClick={() => this.props.history.push('/create-tenant')}>Petition to create Tenant</Button></div>}
          bordered
          dataSource={this.state.pending}
          loading={this.state.loadingPending}
          renderItem={item => (
            <List.Item>
              <Card
                title={item.shortName}
              >
                <p>Full Name: {item.fullName}</p>
                <p>Address: {item.address}</p>
                <p>Contact: {item.contact}</p>
                <p>Website: <a href={item.website}>{item.website}</a></p>
                <p>Summary: {item.orgSummary}</p>
              </Card>
            </List.Item>
          )}
        />
        }
      </div>
    )
  }
  componentDidMount () {
    const uri = config.API_PATH + '/tenants'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*'
      }
    }
    fetcher(uri, options)
      .then(json => this.setState({
        tenants: json.tenantList.filter(tenant => tenant.schemaName !== 'master'),
        loadingTenants: false
      }))
      .catch(error => {
        message.error(error.detail)
        this.setState({loadingTenants: false})
      })
  }
  componentDidUpdate () {
    if (this.state.seePending) {
      const uri = config.API_PATH + '/tenants/pending'
      const options = {
        headers: {
          'Access-Control-Allow-Origin': '*'
        }
      }
      fetcher(uri, options)
        .then(json => this.setState({
          pending: json.pendingTenantList,
          loadingPending: false,
          seePending: false,
          showPendingList: true
        }))
        .catch(err => {
          message.error(err.detail)
          this.setState({
            loadingPending: false,
            seePending: false,
            showPendingList: true
          })
        })
    }
  }
}
