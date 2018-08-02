import React from 'react'
import fetch from 'isomorphic-fetch'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programme: {
        fullName: '',
        shortName: ''
      },
      courses: undefined,
      classes: undefined
    }
  }
  render () {
    return (
      <div>
        <h1> My programme is {this.state.programme.fullName} ({this.state.programme.shortName})</h1>
        <h1>Falta o resto</h1>
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
  }
}
