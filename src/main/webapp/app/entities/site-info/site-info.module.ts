import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SiteInfoComponent } from './list/site-info.component';
import { SiteInfoDetailComponent } from './detail/site-info-detail.component';
import { SiteInfoUpdateComponent } from './update/site-info-update.component';
import { SiteInfoDeleteDialogComponent } from './delete/site-info-delete-dialog.component';
import { SiteInfoRoutingModule } from './route/site-info-routing.module';

@NgModule({
  imports: [SharedModule, SiteInfoRoutingModule],
  declarations: [SiteInfoComponent, SiteInfoDetailComponent, SiteInfoUpdateComponent, SiteInfoDeleteDialogComponent],
  entryComponents: [SiteInfoDeleteDialogComponent],
})
export class SiteInfoModule {}
