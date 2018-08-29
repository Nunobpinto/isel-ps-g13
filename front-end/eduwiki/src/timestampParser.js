export default (timestamp) => {
  if (timestamp) {
    const toPresent = parseDate(timestamp)
    const hourParsed = parseInt(toPresent.hour) + 1
    const hour = hourParsed < 10 ? '0' + hourParsed : hourParsed
    const day = `${toPresent.day}-${toPresent.month + 1}-${toPresent.year}`
    const time = `${hour}h:${toPresent.minute}m`
    return day + '-' + time
  }
  return ''
}

function parseDate (date) {
  let time = date.split('T')
  let year = time[0].split('-')[0]
  let month = (time[0].split('-')[1]) - 1
  let day = time[0].split('-')[2]
  let hour = time[1].split(':')[0]
  let minute = time[1].split(':')[1]
  return {year, month, day, hour, minute}
}
