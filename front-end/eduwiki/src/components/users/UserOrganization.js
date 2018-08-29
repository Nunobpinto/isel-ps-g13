import React from 'react'
import fetcher from '../../fetcher'
import {Card, message} from 'antd'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organization: {
        fullName: '',
        shortName: '',
        address: '',
        contact: ''
      },
      loadingOrganization: true
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My organization'
          loading={this.state.loadingOrganization}
          actions={[
            <p onClick={() => this.props.history.push('/organization')}>
                    See full organization page
            </p>
          ]}
        >
          <h1>{this.state.organization.shortName}</h1>

        </Card>
      </div>
    )
  }
  componentDidMount () {
    const authCookie = this.props.auth
    const options = {
      headers: {
        'Authorization': 'Basic ' + authCookie,
        'Access-Control-Allow-Origin': '*',
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/organization', options)
      .then(json => this.setState({
        organization: json,
        loadingOrganization: false
      }))
      .catch(_ => {
        message.error('Try Later')
        this.setState({loadingOrganization: false})
      })
  }
}
