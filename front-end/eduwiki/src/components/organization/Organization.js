import React from 'react'
import Layout from '../layout/Layout'
import timestampParser from '../../timestampParser'
import ReportOrganization from './ReportOrganization'
import {Row, Col, Card, Button, Popover, message} from 'antd'
import fetcher from '../../fetcher'
import config from '../../config'

class OrganizationDetails extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      id: 0,
      version: 0,
      err: undefined,
      fullName: '',
      shortName: '',
      address: '',
      contact: '',
      website: '',
      createdBy: '',
      timestamp: '',
      voteUp: false,
      voteDown: false,
      redirect: false,
      loading: true,
      user: this.props.user,
      viewVersions: []
    }
  }

  render () {
    return (
      <div>
        <div style={{ padding: '20px' }}>
          <div className='title_div'>
            <h1>{this.state.fullName} - {this.state.shortName} <small>({timestampParser(this.state.timestamp)})</small></h1>
          </div>
          <div className='version_div'>
            <Popover placement='bottom' content={
              <ul>
                {this.state.viewVersions.map(vrs => (
                  <li><a href={`/organization/versions/${vrs.version}`}>version {vrs.version}</a></li>
                ))}
              </ul>
            } trigger='click'>
              <Button type='primary' id='show_reports_btn' icon='down'>
                Version {this.state.version}
              </Button>
            </Popover>
          </div>
          <p>
            <Button id='report_btn' icon='warning' onClick={() => this.setState({createReport: true})}>
              Report Organization
            </Button>
            <Button type='primary' id='show_reports_btn' onClick={() => this.props.history.push('/organization/reports')}>
                      Show all Reports On This Organization
            </Button>

          </p>
          <Row gutter={16}>
            <Col span={5}>
              <Card title='Address' loading={this.state.loading}>
                {this.state.address}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Contact' loading={this.state.loading}>
                {this.state.contact}
              </Card>
            </Col>
            <Col span={5}>
              <Card title='Website' loading={this.state.loading}>
                <a href={this.state.website}>{this.state.website}</a>
              </Card>
            </Col>
          </Row>
        </div>
        {this.state.createReport &&
          <ReportOrganization history={this.props.history} />
        }
      </div>
    )
  }

  componentDidMount () {
    const url = config.API_PATH + '/organization'
    const headers = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(url, headers)
      .then(json => {
        const versionsUri = config.API_PATH + '/organization/versions'
        fetcher(versionsUri, headers)
          .then(versionJson => {
            const versions = versionJson.organizationVersionList
            const viewVersions = versions.filter(version => version.version !== json.version)
            this.setState({
              viewVersions: viewVersions,
              fullName: json.fullName,
              shortName: json.shortName,
              address: json.address,
              contact: json.contact,
              website: json.website,
              createdBy: json.createdBy,
              loading: false,
              version: json.version,
              timestamp: json.timestamp
            })
          })
          .catch(_ => this.setState({
            fullName: json.fullName,
            shortName: json.shortName,
            address: json.address,
            contact: json.contact,
            website: json.website,
            createdBy: json.createdBy,
            loading: false,
            version: json.version,
            timestamp: json.timestamp
          }))
      })
      .catch(err => {
        message.error(err.detail)
        this.setState({err: err, loading: false})
      })
  }
}

export default (props) => (
  <Layout>
    <OrganizationDetails history={props.history} />
  </Layout>

)
