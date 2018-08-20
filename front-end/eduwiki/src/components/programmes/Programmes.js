import React from 'react'
import fetch from 'isomorphic-fetch'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, Form, Input, List, message } from 'antd'
import Cookies from 'universal-cookie'
import CreateProgramme from './CreateProgramme'
const cookies = new Cookies()

export default class extends React.Component {
  constructor (props) {
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
      nameFilter: '',
      createProgrammeFlag: false
    }
    this.handleChange = this.handleChange.bind(this)
    this.voteUp = this.voteUp.bind(this)
    this.voteDown = this.voteDown.bind(this)
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.createStagedProgramme = this.createStagedProgramme.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
    this.filterProgrammesByName = this.filterProgrammesByName.bind(this)
  }

  filterProgrammesByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = [...prevState.programmes]
      array = array.filter(programme => programme.fullName.toLowerCase().includes(name))
      return ({viewProgrammes: array})
    })
  }

  filterStagedByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = [...prevState.staged]
      array = array.filter(programme => programme.fullName.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }

  handleChange (ev) {
    this.setState({
      [ev.target.name]: ev.target.value
    })
  }

  render () {
    return (
      <Layout>
        <div className='container'>
          <div className='left-div'>
            {this.state.error
              ? <p> Error getting all the programmes (Maybe there aren´t any programms) </p>
              : <div>
                <h1>All Programmes in ISEL</h1>
                <p> Filter By Name </p>
                <Input.Search
                  name='nameFilter'
                  placeholder='Search name'
                  onChange={this.filterProgrammesByName}
                />
                <List
                  itemLayout='vertical'
                  size='large'
                  bordered
                  dataSource={this.state.viewProgrammes}
                  renderItem={item => (
                    <List.Item
                      actions={[
                        <IconText
                          type='like-o'
                          id='like_btn'
                          onClick={() =>
                            this.setState({
                              voteUp: true,
                              progID: item.programmeId
                            })}
                          text={item.votes}
                        />,
                        <IconText
                          type='dislike-o'
                          id='dislike_btn'
                          onClick={() =>
                            this.setState({
                              voteDown: true,
                              progID: item.programmeId
                            })}
                        />
                      ]}
                    >
                      <List.Item.Meta
                        title={<Link to={{ pathname: `/programmes/${item.programmeId}` }}> {item.fullName} ({item.shortName})</Link>}
                        description={`Created by ${item.createdBy}`}
                      />
                    </List.Item>
                  )}
                />
              </div>
            }
            <Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({stagedProgrammeView: true})}>Create Programme</Button>
          </div>
          <div className='right-div'>
            {this.state.stagedProgrammeView &&
            <div id='stagedProgrammes'>
              <h1>All staged programmes</h1>
              <p> Filter By Name : </p>
              <Input.Search
                name='stagedNameFilter'
                placeholder='Search name'
                onChange={this.filterStagedByName}
              />
              <List id='staged-list'
                itemLayout='vertical'
                bordered
                dataSource={this.state.viewStaged}
                renderItem={item => (
                  <List.Item>
                    <h1>{`${item.fullName} (${item.shortName})`}</h1>
                    <p>Academic degree: {item.academicDegree}</p>
                    <p>Total Credits: {item.totalCredits}</p>
                    <p>Duration: {item.duration}</p>
                    <p>Created by: {item.createdBy}</p>
                    <IconText
                      type='like-o'
                      id='like_btn'
                      onClick={() =>
                        this.setState({
                          stageID: item.stagedId,
                          voteUpStaged: true
                        })}
                      text={item.votes}
                    />
                    <IconText
                      type='dislike-o'
                      id='dislike_btn'
                      onClick={() =>
                        this.setState({
                          voteDownStaged: true,
                          stageID: item.stagedId
                        })}
                    />
                  </List.Item>
                )}
              />
              <Button type='primary' onClick={() => this.setState({createProgrammeForm: true})}>Still want to create?</Button>
              {this.state.createProgrammeForm && <CreateProgramme action={this.createStagedProgramme} />}
            </div>
            }
          </div>
        </div>
      </Layout>
    )
  }

  componentDidMount () {
    const uri = 'http://localhost:8080/programmes/'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + cookies.get('auth')
      }
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
            programmes: programmes.programmeList,
            viewProgrammes: programmes.programmeList,
            staged: stagedProgrammes.programmeStageList,
            viewStaged: stagedProgrammes.programmeStageList
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

  createStagedProgramme (data) {
    const body = {
      programme_full_name: data.full_name,
      programme_short_name: data.short_name,
      programme_academic_degree: data.academic_degree,
      programme_total_credits: data.total_credits,
      programme_duration: data.duration
    }
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(body)
    }
    const url = 'http://localhost:8080/programmes/stage'
    fetch(url, options)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        return resp.json()
      })
      .then(staged => {
        const newItem = {
          fullName: staged.full_name,
          shortName: staged.short_name,
          academicDegree: staged.academic_degree,
          totalCredits: staged.total_credits,
          duration: staged.duration,
          votes: staged.votes,
          timestamp: staged.timestamp,
          stagedId: staged.stagedId,
          createdBy: staged.createdBy
        }
        message.success('Successfully created staged programme')
        this.setState(prevState => ({
          staged: [...prevState.staged, newItem],
          viewStaged: [...prevState.staged, newItem],
          createProgrammeFlag: false
        }))
      })
      .catch(error => {
        message.error('Error ocurred while creating the programme')
        this.setState({ progError: error, createProgrammeFlag: false })
      })
  }

  voteUp () {
    const voteInput = {
      vote: 'Up',
      created_by: 'ze'
    }
    const progID = this.state.progID
    const url = `http://localhost:8080/programmes/${progID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.programmes]
          const index = newArray.findIndex(programme => programme.programmeId === progID)
          newArray[index].votes = prevState.programmes[index].votes + 1
          return ({
            programmes: newArray,
            voteUp: false
          })
        })
      })
  }

  voteDown () {
    const voteInput = {
      vote: 'Down',
      created_by: 'ze'
    }
    const progID = this.state.progID
    const url = `http://localhost:8080/programmes/${progID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        this.setState(prevState => {
          let newArray = [...prevState.programmes]
          const index = newArray.findIndex(programme => programme.programmeId === progID)
          newArray[index].votes = prevState.programmes[index].votes - 1
          return ({
            programmes: newArray,
            voteDown: false
          })
        })
      })
  }

  voteUpStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const stageID = this.state.stageID
    const url = `http://localhost:8080/programmes/stage/${stageID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        message.success('Successfully voted up')
        this.setState(prevState => {
          let newArray = [...prevState.staged]
          const index = newArray.findIndex(programme => programme.stageID === stageID)
          newArray[index].votes = prevState.staged[index].votes + 1
          return ({
            staged: newArray,
            voteUpStaged: false
          })
        })
      })
      .catch(_ => message.error('Cannot vote'))
  }

  voteDownStaged () {
    const voteInput = {
      vote: 'Down',
      created_by: 'ze'
    }
    const stageID = this.state.stageID
    const url = `http://localhost:8080/programmes/stage/${stageID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + cookies.get('auth')
      },
      body: JSON.stringify(voteInput)
    }
    fetch(url, body)
      .then(resp => {
        if (resp.status >= 400) {
          throw new Error('Unable to access content')
        }
        message.success('Successfully voted down')
        this.setState(prevState => {
          let newArray = [...prevState.staged]
          const index = newArray.findIndex(programme => programme.stageID === stageID)
          newArray[index].votes = prevState.staged[index].votes - 1
          return ({
            staged: newArray,
            voteDownStaged: false
          })
        })
      })
      .catch(_ => message.error('Cannot vote'))
  }

  componentDidUpdate () {
    if (this.state.voteDown) {
      this.voteDown()
    } else if (this.state.voteUp) {
      this.voteUp()
    } else if (this.state.voteDownStaged) {
      this.voteDownStaged()
    } else if (this.state.voteUpStaged) {
      this.voteUpStaged()
    }
  }
}
