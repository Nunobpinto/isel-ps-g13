import React from 'react'
import {Card} from 'antd'
import fetcher from '../../fetcher'
import config from '../../config'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programme: {
        fullName: '',
        shortName: ''
      },
      error: 'Follow a programme',
      loadingProgramme: true
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My Programme'
          loading={this.state.loadingProgramme}
          actions={[
            <p onClick={() => this.props.history.push('/programmes')}>
                    See all programmes
            </p>
          ]}
        >
          {this.state.error
            ? <p>{this.state.error}</p>
            : <a href={`/programmes/${this.state.programme.programmeId}`}>
              <p>
                {this.state.programme.shortName}
              </p>
            </a>
          }

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
        'tenant-uuid': config.TENANT_UUID
      }
    }
    fetcher(config.API_PATH + '/user/programme', options)
      .then(json => this.setState({
        programme: json,
        loadingProgramme: false,
        error: undefined
      }))
      .catch(_ => this.setState({
        error: 'Try following a programme or try later',
        loadingProgramme: false
      }))
  }
}
