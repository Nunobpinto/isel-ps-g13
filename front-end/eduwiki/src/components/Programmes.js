import React from 'react'
import fetch from 'isomorphic-fetch'
import { Link } from 'react-router-dom'
import IconText from './IconText'
import Layout from './Layout'
import { Button, Form, Input, List, Icon, Card, Menu, Dropdown } from 'antd'
const FormItem = Form.Item;

export default class extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      programmes: [],
      viewProgrammes: [],
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
      viewStaged: [],
      stagedNameFilter: '',
      nameFilter:'',
      createProgrammeFlag: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
    this.createProgrammeForm = this.createProgrammeForm.bind(this)
    this.createStagedProgramme = this.createStagedProgramme.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
    this.filterProgrammesByName = this.filterProgrammesByName.bind(this)
    this.showElements = this.showElements.bind(this)
  }

  filterProgrammesByName () {
    const name = this.state.nameFilter
    this.setState (prevState => {
      let array = prevState.programmes
      array = array.filter(programme => programme.shortName.includes(name))
      return ({viewProgrammes: array})
    })
  }

  filterStagedByName () {
    const name = this.state.stagedNameFilter
    this.setState (prevState => {
      let array = prevState.staged
      array = array.filter(programme => programme.shortName.includes(name))
      return ({viewStaged: array})
    })
  }

  handleChange(ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  handleSubmit(ev) {
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

  showElements(id) {
    const element = document.getElementById(id)
    element.className = 'show_staged_resources'
  }

  render() {
    return (
      <Layout
        component={
          <div class='container'>
            <div class='left-div'>
              {this.state.error ?
                <p> Error getting all the programmes (Maybe there aren´t any programms) </p> :
                <div>
                  <h1>All Programmes in ISEL</h1>
                  <p> Filter By Name </p>
                  <Input
                    name='nameFilter'
                    placeholder='Search name'
                    onChange={this.handleChange}
                    onPressEnter={this.filterProgrammesByName}
                  />
                  <List
                    itemLayout='vertical'
                    size='large'
                    bordered
                    dataSource={this.state.viewProgrammes}
                    renderItem={item => (
                      <List.Item
                        actions={[<IconText type="like-o" text={item.votes} />]}
                      >
                        <List.Item.Meta
                          title={<Link to={{ pathname: `/programmes/${item.id}` }}> {item.fullName} ({item.shortName})</Link>}
                          description={`Created by ${item.createdBy}`}
                        />
                      </List.Item>
                    )}
                  />
                </div>
              }
              <Button icon='plus' id='create_btn' type='primary' onClick={() => { this.showElements('stagedProgrammes') }}>Create Programme</Button>
            </div>
            <div class='right-div'>
              <div id='stagedProgrammes' class='hide_staged_resources'>
                <h1>All staged programmes</h1>
                  <p> Filter By Name : </p>
                  <Input
                    name='stagedNameFilter'
                    placeholder="Search name"
                    onChange={this.handleChange}
                    onPressEnter={this.filterStagedByName}
                  />
                <List id='staged-list'
                  grid={{ gutter: 50, column: 2 }}
                  dataSource={this.state.viewStaged}
                  renderItem={item => (
                    <List.Item>
                      <Card title={`${item.fullName} (${item.shortName})`}>
                        <p>Academic degree: {item.academicDegree}</p>
                        <p>Total Credits: {item.totalCredits}</p>
                        <p>Duration: {item.duration}</p>
                        <p>Created by: {item.createdBy}</p>
                      </Card>
                    </List.Item>
                  )}
                />
                <Button type='primary' onClick={() => { this.showElements('formToCreateProgramme') }}>Still want to create?</Button>
              </div>
              {this.createProgrammeForm()}
            </div>
          </div>
        }
      />
    )
  }

  createProgrammeForm = () => (
    <div id='formToCreateProgramme' class='hide_staged_resources'>
      <Form>
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
        <Button type='primary' type='submit' onClick={this.handleSubmit}>Create</Button>
      </Form>
    </div>
  )

  componentDidMount() {
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
            viewProgrammes: programmes,
            staged: stagedProgrammes,
            viewStaged: stagedProgrammes
          }))
          .catch(stagedError => this.setState({
            programmes: programmes,
            viewProgrammes: programmes,
            stagedError: stagedError
          }))
      })
      .catch(error => {
        this.setState({
          error: error
        })
      })
  }

  createStagedProgramme() {
    const data = {
      full_name: this.state.full_name,
      short_name: this.state.short_name,
      academic_degree: this.state.academic_degree,
      total_credits: this.state.total_credits,
      duration: this.state.duration,
      created_by: this.state.created_by,
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
        const newItem = {
          fullName: data.full_name,
          shortName: data.short_name,
          academicDegree: data.academic_degree,
          totalCredits: data.total_credits,
          duration: data.duration,
          createdBy: data.created_by,
          organizationId: 1
        }
        this.setState(prevState => ({
          staged: [...prevState.staged, newItem],
          viewStaged: [...prevState.staged, newItem],
          createProgrammeFlag: false
        }))
      })
      .catch(error => {
        this.setState({ progError: error, createProgrammeFlag: false })
      })
  }

  componentDidUpdate() {
    if (this.state.createProgrammeFlag) {
      this.createStagedProgramme()
    }
  }
}
