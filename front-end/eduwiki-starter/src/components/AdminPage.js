import React from 'react'
import Cookies from 'universal-cookie'
import fetch from 'isomorphic-fetch'
import { message, List, Card, Row, Col } from 'antd'
import Layout from './Layout'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      pending: [],
      loading: true
    }
  }
  render () {
    return (
      <Layout history={this.props.history} auth>
        <h2>All pending Tenants</h2>
        <Row gutter={16}>
          <List
            loading={this.state.loading}
            dataSource={this.state.pending}
            renderItem={item => (
              <Col span={8} key={item.tenantUuid}>
                <List.Item>
                  <Card
                    title={item.shortName}
                    actions={[<a href={`/pending/${item.tenantUuid}`}>Check it's page</a>]}
                  >
                    <p>{item.fullName}</p>
                    <p>{item.address}</p>
                    <p>{item.contact}</p>
                    <p>{item.website}</p>
                    <p>{item.orgSummary}</p>
                    <p>{item.orgSummary}</p>
                  </Card>

                </List.Item>
              </Col>)}
          />
        </Row>
        }
      </Layout>
    )
  }
  componentDidMount () {
    const uri = 'http://localhost:8080/pending'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
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
        pending: json.tenantList,
        loading: false
      }))
      .catch(() => {
        message.error('Error fetching tenants')
        this.setState({
          loading: false
        })
      })
  }
}
