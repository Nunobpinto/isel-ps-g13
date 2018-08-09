import React from 'react'
import fetch from 'isomorphic-fetch'
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
      }
    }
  }
  render () {
    return (
      <div className='side_item'>
        <Card
          title='My organization'
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
        'Access-Control-Allow-Origin': '*'
      }
    }
    fetch('http://localhost:8080/organizations', options)
      .then(resp => {
        if (resp.status >= 400) throw new Error('Error!!!')
        return resp.json()
      })
      .then(json => this.setState({organization: json[0]}))
      .catch(_ => message.error('Something bad happened, please try again'))
  }
}
