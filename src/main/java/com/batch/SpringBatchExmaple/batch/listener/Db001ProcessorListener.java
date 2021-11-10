/**
 * Item Processor Listnener
 * @author memorykghs
 */
public class Db001ProcessorListener implements ItemProcessListener<Cars, CarsDto>{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Db001ProcessorListener.class);

    @Override
    public void beforeProcess(Cars item) {
        LOGGER.info("Manufacturer = {}", item.getManufacturer());
        LOGGER.info("Type = {}", item.getType());
        
    }

    @Override
    public void afterProcess(Cars item, CarsDto result) {
        LOGGER.info("Spread = {}", result.getSpread());
        
    }

    @Override
    public void onProcessError(Cars item, Exception e) {
        LOGGER.info("Db001Processor, error item = {}, {}", item.getManufacturer(), item.getType());
        LOGGER.info("errMsg = {}", e.getMessage());
    }
}
