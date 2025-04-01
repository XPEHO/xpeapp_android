package com.xpeho.xpeapp.ui.components.agenda

import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import java.util.Date

// Common base class to sort the global display by the dates
// of each event and birthday in ascending order
sealed class AgendaItem(val date: Date)

data class AgendaBirthdayItem(val birthday: AgendaBirthday) : AgendaItem(birthday.birthdate)
data class AgendaEventItem(val event: AgendaEvent) : AgendaItem(event.date)
