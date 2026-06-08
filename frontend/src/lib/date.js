export const isoToday = () => new Date().toISOString().slice(0, 10);

export const addDays = (isoDate, amount) => {
  const date = new Date(`${isoDate}T00:00:00`);
  date.setDate(date.getDate() + amount);
  return date.toISOString().slice(0, 10);
};

export const dayCountInclusive = (start, end) => {
  if (!start || !end) return 0;
  const startDate = new Date(`${start}T00:00:00`);
  const endDate = new Date(`${end}T00:00:00`);
  const diff = Math.round((endDate - startDate) / 86400000);
  return diff >= 0 ? diff + 1 : 0;
};

export const isWithinRange = (day, start, end) => day >= start && day <= end;

export const eachDayBetween = (start, end) => {
  if (!start || !end) return [];
  const days = [];
  let cursor = start;
  while (cursor <= end) {
    days.push(cursor);
    cursor = addDays(cursor, 1);
  }
  return days;
};
