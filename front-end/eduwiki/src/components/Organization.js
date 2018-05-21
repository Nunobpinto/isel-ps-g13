import React from 'react'
import CreateOrganization from './CreateOrganization'
import Programme from './Programme'
import fetch from 'isomorphic-fetch'
import {makeCancellable} from './promises'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      organizations: []
    }
  }

  render () {
    return (
      <div>
        <div>
          <ul>
            {this.state.programmes.map(item => <li key={item.id}>
              {`${item.id}`}
            </li>)}
          </ul>
        </div>
        <div>
          <form onSubmit={this.handleSubmit}>
            <input type='text' value={this.state.full_name} onChange={this.handleChange} />
            <input type='text' value={this.state.short_name} onChange={this.handleChange} />
            <input type='text' value={this.state.academic_degree} onChange={this.handleChange} />
            <input type='number' value={this.state.total_credits} onChange={this.handleChange} />
            <input type='number' value={this.state.duration} onChange={this.handleChange} />
            <input type='submit' value='Submit' />
          </form>
        </div>
      </div>
    )
  }

  componentDidMount () {
    if (this.promise) {
      this.promise.cancel()
    }
    this.promise = makeCancellable(fetch(url))
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/vnd.siren+json' || ct.startsWith('application/vnd.siren+json;')) {
          return resp.json().then(json => [resp, json])
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(([resp, json]) => {
        this.setState(prevState => ({
          organizations: [...prevState.organizations, newelement]
        }))
        this.promise = undefined
      })
      .catch(error => {
        this.setState({
          loading: false,
          error: error,
          json: undefined,
          response: undefined
        })
        this.promise = undefined
      })
  }
}