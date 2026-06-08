import { addDays, eachDayBetween, isoToday, isWithinRange } from "../lib/date.js";
import { dateLabel } from "../lib/format.js";
import "./AvailabilityCalendar.css";

const weekdays = ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"];

const hasBlockedDay = (start, end, blockedDays) => eachDayBetween(start, end).some((day) => blockedDays.has(day));

const AvailabilityCalendar = ({ periodos = [], startDate, endDate, onChange, disabled }) => {
  const today = isoToday();
  const days = Array.from({ length: 42 }, (_, index) => addDays(today, index));
  const blockedDays = new Set(
    periodos.flatMap((periodo) => eachDayBetween(periodo.dataInicio, periodo.dataFim))
  );

  const handleSelect = (day) => {
    if (disabled || blockedDays.has(day) || day < today) return;

    if (!startDate || (startDate && endDate)) {
      onChange({ startDate: day, endDate: "" });
      return;
    }

    if (day < startDate) {
      onChange({ startDate: day, endDate: "" });
      return;
    }

    if (hasBlockedDay(startDate, day, blockedDays)) {
      onChange({ startDate: day, endDate: "" });
      return;
    }

    onChange({ startDate, endDate: day });
  };

  return (
    <div className="calendarPanel">
      <div className="calendarHint">
        <span className="free">Livre</span>
        <span className="blocked">Bloqueado</span>
        <span className="selected">Seleção</span>
      </div>
      <div className="calendarGrid">
        {weekdays.map((weekday) => (
          <div key={weekday} className="calendarWeekday">{weekday}</div>
        ))}
        {days.map((day) => {
          const blocked = blockedDays.has(day) || day < today;
          const selected = day === startDate || day === endDate;
          const inRange = startDate && endDate && isWithinRange(day, startDate, endDate);
          return (
            <button
              key={day}
              type="button"
              className={`calendarDay${blocked ? " is-blocked" : ""}${selected ? " is-selected" : ""}${inRange && !selected ? " is-in-range" : ""}`}
              disabled={blocked || disabled}
              onClick={() => handleSelect(day)}
              title={dateLabel(day)}
            >
              {new Date(`${day}T00:00:00`).getDate()}
            </button>
          );
        })}
      </div>
    </div>
  );
};

export default AvailabilityCalendar;
