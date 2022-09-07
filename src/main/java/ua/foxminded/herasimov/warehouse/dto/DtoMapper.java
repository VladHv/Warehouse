package ua.foxminded.herasimov.warehouse.dto;

public interface DtoMapper<T extends Dto, S> {

    T toDto(S entity);

    S toEntity(T dto);
}
