import React from 'react'
import fetch from 'isomorphic-fetch'
import { message, List, Card, Row } from 'antd'
import Layout from './Layout'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      pending: {},
      loading: true
    }
  }
  render () {
    return (
      <Layout history={this.props.history} auth>
        <h2>All pending Tenants</h2>
        <Row gutter={16}>
          <List
            grid={{ gutter: 16, column: 4 }}
            loading={this.state.loading}
            dataSource={this.state.pending}
            renderItem={item => (
              <List.Item>
                <Card
                  title={item.shortName}
                  actions={[<a href={`/pending/${item.tenantUuid}`}>Check it's page</a>]}
                >
                  <p>{item.fullName}</p>
                  <p>Address: {item.address}</p>
                  <p>Contact: {item.contact}</p>
                  <p>Website: {item.website}</p>
                </Card>
              </List.Item>
            )}
          />
        </Row>
      </Layout>
    )
  }
  componentDidMount () {
    const uri = 'http://localhost:8080/tenants/pending/'
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
        loading: false
      }))
      .catch(() => {
        message.error('Error fetching tenants')
        this.setState({loading: false})
      }
      )
  }
}
