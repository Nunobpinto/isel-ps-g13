import React from 'react'
import fetch from 'isomorphic-fetch'
import {Card, message} from 'antd'

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
        'Access-Control-Allow-Origin': '*'
      }
    }
    fetch('http://localhost:8080/user/programme', options)
      .then(resp => {
        if (resp.status >= 400) throw new Error('Error!!!')
        return resp.json()
      })
      .then(json => this.setState({
        programme: `${json.fullName} (${json.shortName})`,
        error: undefined
      }))
      .catch(_ => this.setState({error: 'Try following a programme or try later'}))
  }
}
