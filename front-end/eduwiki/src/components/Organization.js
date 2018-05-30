import React from 'react'
import fetch from 'isomorphic-fetch'
import {makeCancellable} from './promises'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      address: '',
      contact: '',
      createdBy: '',
      redirect: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit (ev) {
    ev.preventDefault()
    const data = {
      full_name: this.state.full_name,
      short_name: this.state.short_name,
      address: this.state.address,
      contact: this.state.contact,
      createdBy: this.state.createdBy
    }
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }
    const url = 'http://localhost:8080/organizations'
    this.promise = makeCancellable(fetch(url, body))
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const ct = resp.headers.get('content-type') || ''
        if (ct === 'application/json') {
          return resp.json().then(json => [resp, json])
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(([resp, json]) => {
        this.setState({
          redirect: true
        })
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

  render () {
    return (
      <div>
        <HttpGet
          url={'http://localhost:8080/organizations'}
          headers={{headers: { 'Access-Control-Allow-Origin': '*' }}}
          render={(result) => (
            <div>
              <HttpGetSwitch
                result={result}
                onJson={json =>
                  (
                    <div>
                      <ul>
                        {json.map(item => <li key={item.id}>
                          <p><strong>Full Name</strong> : {item.fullName}</p>
                          <p><strong>Short Name</strong> : {item.shortName}</p>
                          <p><strong>Address</strong> : {item.address}</p>
                          <p><strong>Contact</strong> :{item.contact}</p>
                          <p><strong>Created By</strong> : {item.createdBy}</p>
                          <p><strong>Votes</strong> : {item.votes}</p>
                        </li>)}
                      </ul>
                    </div>
                  )
                }
                onError={_ => (
                  <div>
                    <form onSubmit={this.handleSubmit}>
                      <input type='text' name='full_name' value={this.state.full_name} onChange={this.handleChange} />
                      <input type='text' name='short_name' value={this.state.short_name} onChange={this.handleChange} />
                      <input type='text' name='address' value={this.state.address} onChange={this.handleChange} />
                      <input type='text' name='contact' value={this.state.contact} onChange={this.handleChange} />
                      <input type='submit' value='Submit' />
                    </form>
                  </div>
                )
                }
              />
            </div>
          )}
        />
      </div>
    )
  }
}
