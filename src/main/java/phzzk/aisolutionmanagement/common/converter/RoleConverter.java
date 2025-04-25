package phzzk.aisolutionmanagement.common.converter;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import phzzk.aisolutionmanagement.common.constants.Role;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class RoleConverter implements AttributeConverter<Set<Role>, String> {
    private static final String SPLIT = ",";

    @Override
    public String convertToDatabaseColumn(Set<Role> roles) {
        if(roles == null || roles.isEmpty()){
            return "";
        }
        return roles.stream()
                .map(role -> role.name())
                .collect(Collectors.joining(SPLIT));
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if(dbData.isEmpty() || dbData.isBlank()){
            return new HashSet<>();
        }
        return Arrays.stream(dbData.split(SPLIT))
                .map(String::trim)
                // map → flatMap 으로 바꿔, 예외 발생 시 빈 스트림 반환
                .flatMap(token -> {
                    try {
                        return Stream.of(Role.valueOf(token));
                    } catch (IllegalArgumentException e) {
                        log.error("RoleConverter: invalid role value='{}', skipping.", token, e);
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toSet());
    }
}
