import React from 'react'
import fetch from 'isomorphic-fetch'
import {Link} from 'react-router-dom'
import Navbar from './Navbar'
import { Button, Form, Input, List } from 'antd'
const FormItem = Form.Item;

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      programmes: [],
      progError: undefined,
      full_name: '',
      short_name: '',
      academic_degree: '',
      total_credits: 0,
      duration: 0,
      createdBy: '',
      redirect: false,
      stagedError: undefined,
      staged: [],
      createProgrammeFlag: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.createProgrammeForm = this.createProgrammeForm.bind(this)
    this.createStagedProgramme = this.createStagedProgramme.bind(this)
    this.showElements = this.showElements.bind(this)
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit (ev) {
    ev.preventDefault()
    this.setState({
      full_name: this.state.full_name,
      short_name: this.state.short_name,
      academic_degree: this.state.academic_degree,
      total_credits: this.state.total_credits,
      duration: this.state.duration,
      created_by: this.state.createdBy,
      organization_id: 1,
      createProgrammeFlag: true
    })
  }

  showElements (id) {
    const element = document.getElementById(id)
    element.className='show_staged_resources'
  }

  render () {
    return (
      <div>
        <Navbar />
        <div class='container'>
          <div class='left-div'>
            {this.state.error ? <p>
                  Error getting all the programmes (Maybe there aren´t any programms)
            </p>
            : <div>
                <h1>All Programmes in ISEL</h1>
                <List 
                  bordered
                  dataSource={this.state.programmes}
                  id='list'
                  renderItem={ item => (
                    <List.Item>
                      <Link to={{pathname: `/programmes/${item.id}`}}>
                        {item.fullName} ({item.shortName}) - Created By {item.createdBy}
                      </Link>
                    </List.Item>                    
                    )
                  }
                />
              </div>
            }
          <Button icon='plus' id='create_btn' type='primary' onClick={() => {this.showElements('stagedProgrammes')}}>Create Programme</Button>
          </div>
          <div class='right-div'>
            <div id='stagedProgrammes' class='hide_staged_resources'>
              <div class="vl" />
              <h1>All staged programmes</h1>
              <ul id='staged-list'>
                {this.state.staged.map(item =>
                  <li key={item.id}>
                    {item.fullName} ({item.shortName}) - Created By {item.createdBy}
                  </li>
                )}
              </ul>
              <Button type='primary' onClick={() => {this.showElements('formToCreateProgramme')}}>Still Want to Create ?</Button>
            </div>
            {this.createProgrammeForm()}
          </div>
      </div>
      </div>
    )
  }

  createProgrammeForm = () =>(
      <div id='formToCreateProgramme' class='hide_staged_resources'>
        <Form onSubmit={this.handleSubmit}>
          Full name: <br />
          <Input name='full_name' onChange={this.handleChange} />
          <br />
          Short name: <br />
          <Input name='short_name' onChange={this.handleChange} />
          <br />
          Academic Degree: <br />
          <Input name='academic_degree' onChange={this.handleChange} />
          <br />
          Total Credits: <br />
          <input type='number' name='total_credits' onChange={this.handleChange} />
          <br />
          Duration: <br />
          <input type='number' name='duration' onChange={this.handleChange} />
          <br />
          Created By: <br />
          <Input name='created_by' onChange={this.handleChange} />
          <br />
          <Button type='primary' type='submit' value='Submit'>Create</Button>
        </Form>
      </div>
    )

  componentDidMount () {
    const uri = 'http://localhost:8080/programmes/'
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
          return resp.json()
        }
        throw new Error(`unexpected content type ${ct}`)
      })
      .then(programmes => {
        const stagedUri = 'http://localhost:8080/programmes/stage'
        fetch(stagedUri, header)
          .then(response => {
            if (response.status >= 400) {
              throw new Error('Unable to access content')
            }
            const ct = response.headers.get('content-type') || ''
            if (ct === 'application/json' || ct.startsWith('application/json;')) {
              return response.json()
            }
            throw new Error(`unexpected content type ${ct}`)
          })
          .then(stagedProgrammes => this.setState({
            programmes: programmes,
            staged: stagedProgrammes
          }))
          .catch(stagedError => this.setState({
            programmes: programmes,
            stagedError: stagedError
          }))
      })
      .catch(error => {
        this.setState({
          error: error
        })
      })
  }

  createStagedProgramme () {
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
    const url = 'http://localhost:8080/programmes/stage'
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        const list = document.getElementById('staged-list')
        const newElement = document.createElement('li')
        newElement.innerHTML = `${data.full_name} (${data.short_name}) - Created By ${data.created_by}`
        list.appendChild(newElement)
        this.setState({createProgrammeFlag: false})
      })
      .catch(error => {
        this.setState({progError: error, createProgrammeFlag: false})
      })
  }

  componentDidUpdate () {
    if (this.state.createProgrammeFlag) {
      this.createStagedProgramme()
    }
  }
}
