package de.mrrobworks.springbootangular.backend.approle;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppRoleServiceImpl implements AppRoleService {

  @NonNull private final AppRoleRepository appRoleRepository;
  @NonNull private final AppRoleMapper appRoleMapper;

  @Override
  public AppRoleDto getAppRole(String id) {
    AppRole appRole = appRoleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    return appRoleMapper.fromAppRole(appRole);
  }

  @Override
  public Map<GrantedAuthority, AppRole> getMappedAppRoles() {
    var mappedAppRoles = new HashMap<GrantedAuthority, AppRole>();

    List<AppRole> appRoles = appRoleRepository.findAll();
    for (AppRole appRole : appRoles) {
      mappedAppRoles.put(new SimpleGrantedAuthority(appRole.getId()), appRole);
    }

    return mappedAppRoles;
  }

  @Override
  public List<AppRoleDto> getAppRoles() {
    return appRoleMapper.appRoleListToAppRoleDtoList(appRoleRepository.findAll());
  }

  @Override
  public AppRoleDto createOrUpdateAppRole(AppRoleDto appRoleDto) {
    AppRole appRole = appRoleRepository.findById(appRoleDto.getId()).orElse(new AppRole());
    appRoleMapper.updateDtoToAppRole(appRoleDto, appRole);
    AppRole savedAppRole = appRoleRepository.save(appRole);
    return appRoleMapper.fromAppRole(savedAppRole);
  }

  @Override
  public void deleteAppRole(@NonNull String id) {
    appRoleRepository.deleteById(id);
  }
}
