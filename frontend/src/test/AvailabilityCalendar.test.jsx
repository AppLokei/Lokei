import { act, fireEvent, render, screen, waitFor } from "@testing-library/react";
import { useState } from "react";

import AvailabilityCalendar from "../components/AvailabilityCalendar.jsx";
import { addDays, isoToday } from "../lib/date.js";
import { dateLabel } from "../lib/format.js";

const CalendarHarness = ({ periodos }) => {
  const [dates, setDates] = useState({ startDate: "", endDate: "" });
  return (
    <>
      <AvailabilityCalendar periodos={periodos} startDate={dates.startDate} endDate={dates.endDate} onChange={setDates} />
      <div data-testid="range">{dates.startDate}::{dates.endDate}</div>
    </>
  );
};

describe("AvailabilityCalendar", () => {
  it("bloqueia dias reservados e permite selecionar intervalo livre", async () => {
    const today = isoToday();
    const blockedStart = addDays(today, 2);
    const blockedEnd = addDays(today, 3);
    const freeStart = addDays(today, 5);
    const freeEnd = addDays(today, 6);

    render(
      <CalendarHarness
        periodos={[{ aluguelId: 1, dataInicio: blockedStart, dataFim: blockedEnd, status: "CONFIRMADO" }]}
      />
    );

    expect(screen.getByTitle(dateLabel(blockedStart))).toBeDisabled();

    await act(async () => {
      fireEvent.click(screen.getByTitle(dateLabel(freeStart)));
    });
    await waitFor(() => {
      expect(screen.getByTestId("range")).toHaveTextContent(`${freeStart}::`);
    });

    await act(async () => {
      fireEvent.click(screen.getByTitle(dateLabel(freeEnd)));
    });

    expect(screen.getByTestId("range")).toHaveTextContent(`${freeStart}::${freeEnd}`);
  });
});
