import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      createdBy: '',
      votes: 0,
      timestamp: '',
      courses: [],
      voteType: undefined,
      error: undefined
    }
  }

  render () {
    return (
      <div>
        <div>
          <h1>{this.state.full_name} - {this.state.short_name} <small>({this.state.timestamp})</small> </h1>
          <button>About</button>
          <div>
            <p>Created By : {this.state.createdBy}</p>
            <p>Votes : {this.state.votes}</p>
          </div>
        </div>
      </div>
    )
  }

  componentDidMount () {
    const id = this.props.match.params.id
    const uri = 'http://localhost:8080/courses/' + id
    const header = {
      headers: { 'Access-Control-Allow-Origin': '*' }
    }
    fetch(uri, header)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json' || ct.startsWith('application/json;')) {
          return resp.json().then(json => [resp, json])
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(([resp, json]) => {
        this.setState({
          full_name: json.fullName,
          short_name: json.shortName,
          createdBy: json.createdBy,
          timestamp: json.timestamp,
          votes: json.votes,
          progError: undefined,
          courseError: undefined
        })
      })
      .catch(error => {
        this.setState({
          progError: error
        })
      })
  }

  componentDidUpdate () {

  }
}
