import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { YorumComponent } from './list/yorum.component';
import { YorumDetailComponent } from './detail/yorum-detail.component';
import { YorumUpdateComponent } from './update/yorum-update.component';
import { YorumDeleteDialogComponent } from './delete/yorum-delete-dialog.component';
import { YorumRoutingModule } from './route/yorum-routing.module';

@NgModule({
  imports: [SharedModule, YorumRoutingModule],
  declarations: [YorumComponent, YorumDetailComponent, YorumUpdateComponent, YorumDeleteDialogComponent],
  entryComponents: [YorumDeleteDialogComponent],
})
export class YorumModule {}
