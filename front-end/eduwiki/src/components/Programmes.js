import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'
import HttpGet from './http-get'
import HttpGetSwitch from './http-get-switch'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      full_name: '',
      short_name: '',
      academic_degree: '',
      total_credits: 0,
      duration: 0,
      createdBy: '',
      redirect: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.createProgrammeForm = this.createProgrammeForm.bind(this)
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
      academic_degree: this.state.academic_degree,
      total_credits: this.state.total_credits,
      duration: this.state.duration,
      created_by: this.state.createdBy,
      organization_id: 1
    }
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }
    const url = 'http://localhost:8080/programmes'
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.props.history.push(`/programmes`)
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
          url={'http://localhost:8080/programmes'}
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
                          <Link to={{pathname: `/programmes/${item.id}`}}>
                            {`${item.shortName} Created By ${item.createdBy}`}
                          </Link>
                        </li>)}
                      </ul>
                      <form onSubmit={this.handleSubmit}>
                        <input placeholder='Full Name' type='text' name='full_name' value={this.state.full_name} onChange={this.handleChange} />
                        <input placeholder='Short Name' type='text' name='short_name' value={this.state.short_name} onChange={this.handleChange} />
                        <input placeholder='Academic Degree' type='text' name='academic_degree' value={this.state.academic_degree} onChange={this.handleChange} />
                        <input placeholder='Total Credits' type='number' name='total_credits' value={this.state.total_credits} onChange={this.handleChange} />
                        <input placeholder='Duration' type='number' name='duration' value={this.state.duration} onChange={this.handleChange} />
                        <input placeholder='Created By' type='text' name='createdBy' value={this.state.createdBy} onChange={this.handleChange} />
                        <input type='submit' value='Submit' />
                      </form>
                    </div>
                  )
                }
                onError={_ => (
                  <div>
                    <h1>Create Programme</h1>
                    {this.createProgrammeForm()}
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

  createProgrammeForm () {
    return (
      <form onSubmit={this.handleSubmit}>
        <input placeholder='insert Full Name' type='text' value={this.state.full_name} onChange={this.handleChange} />
        <input placeholder='insert Short Name' type='text' value={this.state.short_name} onChange={this.handleChange} />
        <input placeholder='insert Academic Degrees' type='text' value={this.state.academic_degree} onChange={this.handleChange} />
        <input placeholder='insert Total Credits' type='number' value={this.state.total_credits} onChange={this.handleChange} />
        <input placeholder='insert Duration' type='number' value={this.state.duration} onChange={this.handleChange} />
        <input type='submit' value='Submit' />
      </form>
    )
  }
}
