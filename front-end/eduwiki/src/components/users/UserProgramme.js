import React from 'react'
import {Card} from 'antd'
import fetcher from '../../fetcher'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programme: {
        fullName: '',
        shortName: ''
      },
      error: 'Follow a programme'
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My Programme'
          actions={[
            <p onClick={() => this.props.history.push('/programmes')}>
                    See all programmes
            </p>
          ]}
        >
          {this.state.error
            ? <p>{this.state.error}</p>
            : <p onClick={() => this.props.history.push(`/programmes/${this.state.programme.programmeId}`)}>
              {`${this.state.programme.fullName} (${this.state.programme.shortName})`}
            </p>
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
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher('http://localhost:8080/user/programme', options)
      .then(json => this.setState({
        programme: `${json.fullName} (${json.shortName})`,
        error: undefined
      }))
      .catch(_ => this.setState({error: 'Try following a programme or try later'}))
  }
}
