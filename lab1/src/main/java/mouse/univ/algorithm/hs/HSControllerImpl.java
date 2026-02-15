package mouse.univ.algorithm.hs;

import mouse.univ.algorithm.ControllerImpl;

import java.util.List;

public class HSControllerImpl extends ControllerImpl<HSMessage> implements HSController {
    public HSControllerImpl(List<Long> uids) {
        super(uids);
    }
}
