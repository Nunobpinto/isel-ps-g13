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
      }
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
          <p>{`${this.state.programme.fullName} (${this.state.programme.shortName})`}</p>
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
      .then(json => this.setState({programme: json}))
      .catch(_ => message.error('Something bad happened, please try again'))
  }
}