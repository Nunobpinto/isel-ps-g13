import fetch from 'isomorphic-fetch'

export default (path, headers) =>
  new Promise((resolve, reject) => {
    fetch(path, headers)
      .then(resp => {
        if (resp.status >= 400) {
          return resp.json().then(error =>
            reject(error)
          )
        }
        return resolve(resp.json())
      })
      .catch(err => reject(err))
  })
