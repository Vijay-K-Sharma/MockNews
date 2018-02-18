package mock.mocknews;
import javax.inject.Singleton;

import dagger.Component;
import mock.mocknews.service.ServiceDataClass;
import mock.mocknews.utils.JsonUtils;


@Singleton
@Component(modules = {JsonUtils.class ,ServiceDataClass.class})
public interface MockNewsComponent {
    JsonUtils getJsonUtils();
    ServiceDataClass getService();
}