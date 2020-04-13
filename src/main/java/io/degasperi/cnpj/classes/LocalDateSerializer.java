package io.degasperi.cnpj.classes;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {
	@Override
	public void serialize(LocalDate date, JsonGenerator g, SerializerProvider provider) throws IOException {
		g.writeString(date.toString());
	}
}
