import React from 'react'
import Layout from '../layout/Layout'
import OrganizationReports from './OrganizationReports'
import OrganizationVersions from './OrganizationVersions'
import ReportOrganization from './ReportOrganization'
import {Row, Col, Card, Button, Tooltip, Popover, message} from 'antd'
import Cookies from 'universal-cookie'
import fetcher from '../../fetcher'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organization: undefined,
      id: 0,
      version: 0,
      err: undefined,
      full_name: '',
      short_name: '',
      address: '',
      contact: '',
      createdBy: '',
      voteUp: false,
      voteDown: false,
      redirect: false
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
              <Popover placement='bottom' content={<OrganizationVersions auth={cookies.get('auth')} id={this.state.id} version={this.state.version} />} trigger='click'>
                <Button type='primary' id='show_reports_btn' icon='down'>
              Version {this.state.versionNumber}
                </Button>
              </Popover>
            </div>
            <p>
                Votes : {this.state.votes}
              <Tooltip placement='bottom' title={`Vote Up on ${this.state.organization.shortName}`}>
                <Button id='like_btn' shape='circle' icon='like' onClick={() => this.setState({voteUp: true})} />
              </Tooltip>
              <Tooltip placement='bottom' title={`Vote Down on ${this.state.organization.shortName}`}>
                <Button id='dislike_btn' shape='circle' icon='dislike' onClick={() => this.setState({voteDown: true})} />
              </Tooltip>
              <Popover content={<ReportOrganization />} trigger='click'>
                <Tooltip placement='bottom' title='Report this Organization'>
                  <Button id='report_btn' shape='circle' icon='warning' />
                </Tooltip>
              </Popover>
              <Popover placement='bottomLeft' content={<OrganizationReports />} trigger='click'>
                <Button type='primary' id='show_reports_btn'>
                    Show all Reports On This Organization
                </Button>
              </Popover>
            </p>
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
          </div>

        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const url = 'http://localhost:8080/organization'
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, headers)
      .then(json => this.setState({
        organization: json,
        version: json.version,
        votes: json.votes
      }))
      .catch(err => {
        message.error('Cannot access organization details')
        this.setState({err: err})
      })
  }
}
